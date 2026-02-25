package com.suchelin.shared.data.repository

import com.suchelin.shared.model.PostData
import com.suchelin.shared.model.StoreData
import com.suchelin.shared.model.StoreMenuData
import kotlinx.coroutines.flow.StateFlow

interface StoreRepository {
    suspend fun getStores(): List<StoreData>
}

interface MenuRepository {
    suspend fun getMenus(): Map<Int, StoreMenuData>
}

interface PostRepository {
    suspend fun getPosts(): List<PostData>
    suspend fun addPost(post: String)
}

interface VoteRepository {
    val votes: StateFlow<Map<String, Long>>
    suspend fun refresh()
    suspend fun vote(key: String)
}

interface AuthRepository {
    suspend fun signInAnonymously(): String?
}

interface DailyLimitRepository {
    suspend fun canPostToday(): Boolean
    suspend fun markPostUsed()
    suspend fun canVoteToday(storeId: Int): Boolean
    suspend fun markVoteUsed(storeId: Int)
}
