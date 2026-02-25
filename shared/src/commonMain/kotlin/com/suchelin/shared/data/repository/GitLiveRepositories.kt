package com.suchelin.shared.data.repository

import com.suchelin.shared.model.PostData
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.model.StoreDetail
import com.suchelin.shared.model.StoreMenuDetail
import com.suchelin.shared.model.StoreMenuData
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

class GitLiveAuthRepository : AuthRepository {
    override suspend fun signInAnonymously(): String? {
        return runCatching {
            Firebase.auth.signInAnonymously()
            Firebase.auth.currentUser?.uid
        }.getOrNull()
    }
}
