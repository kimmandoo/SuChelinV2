package com.suchelin.shared.data.repository

import com.suchelin.shared.model.PostData
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.model.StoreDetail
import com.suchelin.shared.model.StoreMenuDetail
import com.suchelin.shared.model.StoreMenuData
import com.suchelin.shared.util.StoreFilter
import com.suchelin.shared.util.nowTimeKey
import com.suchelin.shared.util.todayDocName
import com.suchelin.shared.util.todayKoreanLabel
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import dev.gitlive.firebase.database.database
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

private suspend fun ensureAuthenticated() {
    if (Firebase.auth.currentUser == null) {
        runCatching { Firebase.auth.signInAnonymously() }
    }
}

class GitLiveStoreRepository : StoreRepository {
    override suspend fun getStores(): List<StoreData> {
        return runCatching {
            ensureAuthenticated()
            val snapshot = Firebase.firestore.collection("store").snapshots.first()
            snapshot.documents.mapNotNull { doc ->
                val documentId = doc.id.toIntOrNull()
                val path = runCatching { doc.get<Int>("path") }.getOrNull()
                val storeId = documentId ?: path ?: return@mapNotNull null
                val sortOrder = path ?: storeId

                sortOrder to StoreData(
                    storeId = storeId,
                    storeDetailData = StoreDetail(
                        name = runCatching { doc.get<String>("name") }.getOrDefault(""),
                        detail = runCatching { doc.get<String>("detail") }.getOrDefault(""),
                        imageUrl = runCatching { doc.get<String>("imageUrl") }.getOrDefault(""),
                        latitude = runCatching { doc.get<Double>("latitude") }.getOrDefault(0.0),
                        longitude = runCatching { doc.get<Double>("longitude") }.getOrDefault(0.0),
                        menuImageUrl = runCatching { doc.get<String>("menuImageUrl") }.getOrNull(),
                        type = runCatching { doc.get<String>("type") }.getOrDefault("restaurant"),
                        isClosed = runCatching { doc.get<Boolean>("isClosed") }.getOrDefault(false),
                    ),
                )
            }.sortedBy { it.first }.map { it.second }
        }.getOrElse { emptyList() }
    }
}

class GitLiveMenuRepository : MenuRepository {
    override suspend fun getMenus(): Map<Int, StoreMenuData> {
        return runCatching {
            ensureAuthenticated()
            val snapshot = Firebase.firestore.collection("menu").snapshots.first()
            snapshot.documents.mapNotNull { doc ->
                val path = doc.id.toIntOrNull()
                    ?: runCatching { doc.get<Int>("path") }.getOrNull()
                    ?: return@mapNotNull null

                val isImage = runCatching { doc.get<Boolean>("image") }.getOrDefault(false)
                val menuItems: List<Any> = if (isImage) {
                    runCatching { doc.get<List<String>>("menu") }
                        .getOrDefault(emptyList())
                        .map { it as Any }
                } else {
                    runCatching { doc.get<List<Map<String, String>>>("menu") }
                        .getOrDefault(emptyList())
                        .map { item ->
                            StoreMenuDetail(
                                menuName = item["menuName"] ?: "",
                                menuPrice = item["menuPrice"] ?: "",
                            ) as Any
                        }
                }

                path to StoreMenuData(
                    image = isImage,
                    storeMenu = menuItems,
                    tel = runCatching { doc.get<String>("tel") }.getOrDefault(""),
                )
            }.toMap()
        }.getOrElse { emptyMap() }
    }
}

class GitLivePostRepository : PostRepository {
    override suspend fun getPosts(): List<PostData> {
        return runCatching {
            ensureAuthenticated()
            val document = Firebase.firestore.collection("suggest").document(todayDocName()).get()
            if (!document.exists) {
                val initPost = "오늘은 ${todayKoreanLabel()} 입니다"
                val initData = mapOf(nowTimeKey() to initPost)
                Firebase.firestore.collection("suggest").document(todayDocName()).set(initData)
                listOf(PostData(date = "", post = initPost))
            } else {
                runCatching { document.data<Map<String, String>>() }
                    .getOrDefault(emptyMap())
                    .entries
                    .sortedByDescending { it.key }
                    .map { (time, post) -> PostData(date = time, post = post) }
            }
        }.getOrElse { emptyList() }
    }

    override suspend fun addPost(post: String) {
        runCatching {
            ensureAuthenticated()
            val document = Firebase.firestore.collection("suggest").document(todayDocName())
            val timeKey = nowTimeKey()
            runCatching { document.update(timeKey to post) }
                .onFailure { document.set(mapOf(timeKey to post)) }
        }
    }
}

class GitLiveVoteRepository : VoteRepository {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _votes = MutableStateFlow<Map<String, Long>>(emptyMap())
    override val votes: StateFlow<Map<String, Long>> = _votes.asStateFlow()
    private var observing = false

    private fun toVoteMap(snapshot: dev.gitlive.firebase.database.DataSnapshot): Map<String, Long> {
        val map = mutableMapOf<String, Long>()
        snapshot.children.forEach { child ->
            val childKey = child.key ?: return@forEach
            map[childKey] = runCatching { child.value<Long>() }.getOrDefault(0L)
        }
        return map
    }

    override suspend fun refresh() {
        if (observing) return
        observing = true

        scope.launch {
            runCatching {
                ensureAuthenticated()
                Firebase.database.reference("").valueEvents.collect { snapshot ->
                    _votes.value = toVoteMap(snapshot)
                }
            }.onFailure {
                _votes.value = emptyMap()
                observing = false
            }
        }
    }

    override suspend fun vote(key: String) {
        runCatching {
            ensureAuthenticated()
            val ref = Firebase.database.reference("").child(key)
            val current = _votes.value[key] ?: 0L
            ref.setValue(current + 1L)
            refresh()
        }
    }
}

class GitLiveReportRepository : ReportRepository {
    private val collection get() = Firebase.firestore.collection("reports")

    private suspend fun applyNewStoreReport(
        storeName: String,
        storeType: String,
        content: String,
    ) {
        val allowedTypes = setOf(StoreFilter.RESTAURANT.type, StoreFilter.CAFE.type, StoreFilter.PUB.type)
        if (storeType !in allowedTypes) return

        val storeSnapshot = Firebase.firestore.collection("store").snapshots.first()
        val maxStoreId = storeSnapshot.documents.maxOfOrNull { doc ->
            doc.id.toIntOrNull() ?: runCatching { doc.get<Int>("path") }.getOrNull() ?: 0
        } ?: 0
        val newStoreId = maxStoreId + 1

        val newStore = mapOf(
            "name" to storeName,
            "detail" to content,
            "imageUrl" to "",
            "latitude" to 0.0,
            "longitude" to 0.0,
            "menuImageUrl" to null,
            "type" to storeType,
            "isClosed" to false,
            "path" to newStoreId,
        )

        val newMenu = mapOf(
            "image" to false,
            "menu" to emptyList<Map<String, String>>(),
            "tel" to "",
            "path" to newStoreId,
        )

        Firebase.firestore.collection("store").document(newStoreId.toString()).set(newStore)
        Firebase.firestore.collection("menu").document(newStoreId.toString()).set(newMenu)
    }

    private suspend fun applyPriceChangeReport(
        storeId: Int,
        targetMenuName: String,
        newPrice: String,
    ) {
        val menuDocRef = Firebase.firestore.collection("menu").document(storeId.toString())
        val menuDoc = menuDocRef.get()
        val isImage = runCatching { menuDoc.get<Boolean>("image") }.getOrDefault(false)
        if (isImage) return

        val currentMenus = runCatching { menuDoc.get<List<Map<String, String>>>("menu") }
            .getOrDefault(emptyList())

        val hasTarget = currentMenus.any { it["menuName"] == targetMenuName }
        if (!hasTarget) return

        val updatedMenus = currentMenus.map { menu ->
            if (menu["menuName"] == targetMenuName) {
                menu.toMutableMap().apply { this["menuPrice"] = newPrice }
            } else {
                menu
            }
        }

        menuDocRef.update("menu" to updatedMenus)
    }

    private suspend fun applyClosedReport(storeId: Int) {
        val storeDocRef = Firebase.firestore.collection("store").document(storeId.toString())
        val storeDoc = storeDocRef.get()
        val currentDetail = runCatching { storeDoc.get<String>("detail") }.getOrDefault("")
        val closedPrefix = "[폐업] "
        val updatedDetail = if (currentDetail.startsWith(closedPrefix)) currentDetail else "$closedPrefix$currentDetail"

        storeDocRef.update(
            "isClosed" to true,
            "detail" to updatedDetail,
        )
    }

    private suspend fun applyReportIfNeeded(doc: dev.gitlive.firebase.firestore.DocumentSnapshot): Boolean {
        val typeName = runCatching { doc.get<String>("type") }.getOrNull() ?: return false
        val type = runCatching { com.suchelin.shared.model.ReportType.valueOf(typeName) }.getOrNull() ?: return false
        when (type) {
            com.suchelin.shared.model.ReportType.NEW_STORE -> {
                val storeName = runCatching { doc.get<String>("storeName") }.getOrDefault("")
                val storeType = runCatching { doc.get<String>("newStoreType") }.getOrDefault("")
                val content = runCatching { doc.get<String>("content") }.getOrDefault("")
                if (storeName.isNotBlank() && storeType.isNotBlank()) {
                    applyNewStoreReport(storeName = storeName, storeType = storeType, content = content)
                    return true
                }
            }

            com.suchelin.shared.model.ReportType.PRICE_CHANGE -> {
                val storeId = runCatching { doc.get<Int>("storeId") }.getOrNull() ?: return false
                val targetMenuName = runCatching { doc.get<String>("targetMenuName") }.getOrNull() ?: return false
                val newPrice = runCatching { doc.get<String>("newPrice") }.getOrNull() ?: return false
                if (targetMenuName.isNotBlank() && newPrice.isNotBlank()) {
                    applyPriceChangeReport(
                        storeId = storeId,
                        targetMenuName = targetMenuName,
                        newPrice = newPrice,
                    )
                    return true
                }
            }

            com.suchelin.shared.model.ReportType.CLOSED -> {
                val storeId = runCatching { doc.get<Int>("storeId") }.getOrNull() ?: return false
                applyClosedReport(storeId)
                return true
            }

            else -> {
                return false
            }
        }

        return false
    }

    override suspend fun submitReport(report: com.suchelin.shared.model.ReportData) {
        runCatching {
            ensureAuthenticated()
            val uid = Firebase.auth.currentUser?.uid ?: "unknown"
            val data = mapOf(
                "type" to report.type.name,
                "storeId" to report.storeId,
                "storeName" to report.storeName,
                "newStoreType" to report.newStoreType,
                "content" to report.content,
                "targetMenuName" to report.targetMenuName,
                "previousPrice" to report.previousPrice,
                "newPrice" to report.newPrice,
                "date" to report.date,
                "uid" to uid,
                "upvotes" to 0,
                "downvotes" to 0,
                "votedUpUids" to emptyList<String>(),
                "votedDownUids" to emptyList<String>(),
                "applied" to false,
            )
            collection.add(data)
        }
    }

    override suspend fun getReports(): List<com.suchelin.shared.model.ReportData> {
        return runCatching {
            ensureAuthenticated()
            val snapshot = collection.snapshots.first()
            snapshot.documents.mapNotNull { doc ->
                val typeName = runCatching { doc.get<String>("type") }.getOrNull() ?: return@mapNotNull null
                val type = runCatching { com.suchelin.shared.model.ReportType.valueOf(typeName) }.getOrNull() ?: return@mapNotNull null
                com.suchelin.shared.model.ReportData(
                    id = doc.id,
                    type = type,
                    storeId = runCatching { doc.get<Int>("storeId") }.getOrNull(),
                    storeName = runCatching { doc.get<String>("storeName") }.getOrDefault(""),
                    newStoreType = runCatching { doc.get<String>("newStoreType") }.getOrNull(),
                    content = runCatching { doc.get<String>("content") }.getOrDefault(""),
                    targetMenuName = runCatching { doc.get<String>("targetMenuName") }.getOrNull(),
                    previousPrice = runCatching { doc.get<String>("previousPrice") }.getOrNull(),
                    newPrice = runCatching { doc.get<String>("newPrice") }.getOrNull(),
                    date = runCatching { doc.get<String>("date") }.getOrDefault(""),
                    uid = runCatching { doc.get<String>("uid") }.getOrDefault(""),
                    upvotes = runCatching { doc.get<Int>("upvotes") }.getOrDefault(0),
                    downvotes = runCatching { doc.get<Int>("downvotes") }.getOrDefault(0),
                    votedUpUids = runCatching { doc.get<List<String>>("votedUpUids") }.getOrDefault(emptyList()),
                    votedDownUids = runCatching { doc.get<List<String>>("votedDownUids") }.getOrDefault(emptyList()),
                    applied = runCatching { doc.get<Boolean>("applied") }.getOrDefault(false),
                )
            }.sortedByDescending { it.date }
        }.getOrElse { emptyList() }
    }

    override suspend fun upvoteReport(reportId: String) {
        runCatching {
            ensureAuthenticated()
            val uid = Firebase.auth.currentUser?.uid ?: return
            val docRef = collection.document(reportId)
            val doc = docRef.get()
            val upUids = runCatching { doc.get<List<String>>("votedUpUids") }.getOrDefault(emptyList())
            val downUids = runCatching { doc.get<List<String>>("votedDownUids") }.getOrDefault(emptyList())
            val alreadyApplied = runCatching { doc.get<Boolean>("applied") }.getOrDefault(false)

            if (uid in upUids) return // already upvoted

            val newUpUids = upUids + uid
            val newUpvotes = newUpUids.size
            val updates = mutableMapOf<String, Any?>(
                "votedUpUids" to newUpUids,
                "upvotes" to newUpvotes,
            )

            // remove from downvotes if was there
            if (uid in downUids) {
                val newDownUids = downUids - uid
                updates["votedDownUids"] = newDownUids
                updates["downvotes"] = newDownUids.size
            }

            val typeName = runCatching { doc.get<String>("type") }.getOrNull()
            val type = typeName?.let { runCatching { com.suchelin.shared.model.ReportType.valueOf(it) }.getOrNull() }
            val autoApplyTypes = setOf(
                com.suchelin.shared.model.ReportType.PRICE_CHANGE,
                com.suchelin.shared.model.ReportType.CLOSED,
                com.suchelin.shared.model.ReportType.NEW_STORE,
            )

            // auto-apply only for selected types at 10 upvotes
            if (newUpvotes >= 10 && !alreadyApplied) {
                if (type in autoApplyTypes) {
                    val applied = applyReportIfNeeded(doc)
                    if (applied) {
                        updates["applied"] = true
                    }
                }
            }

            docRef.update(updates)
        }
    }

    override suspend fun downvoteReport(reportId: String) {
        runCatching {
            ensureAuthenticated()
            val uid = Firebase.auth.currentUser?.uid ?: return
            val docRef = collection.document(reportId)
            val doc = docRef.get()
            val upUids = runCatching { doc.get<List<String>>("votedUpUids") }.getOrDefault(emptyList())
            val downUids = runCatching { doc.get<List<String>>("votedDownUids") }.getOrDefault(emptyList())

            if (uid in downUids) return // already downvoted

            val newDownUids = downUids + uid
            val updates = mutableMapOf<String, Any?>(
                "votedDownUids" to newDownUids,
                "downvotes" to newDownUids.size,
            )

            // remove from upvotes if was there
            if (uid in upUids) {
                val newUpUids = upUids - uid
                updates["votedUpUids"] = newUpUids
                updates["upvotes"] = newUpUids.size
            }

            docRef.update(updates)
        }
    }
}

class GitLiveAuthRepository : AuthRepository {
    override suspend fun signInAnonymously(): String? {
        return runCatching {
            Firebase.auth.signInAnonymously()
            Firebase.auth.currentUser?.uid
        }.getOrNull()
    }
}
