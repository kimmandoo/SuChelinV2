package com.suchelin.shared.di

import android.content.Context
import com.suchelin.shared.data.repository.AuthRepository
import com.suchelin.shared.data.repository.DailyLimitRepository
import com.suchelin.shared.data.repository.GitLiveAuthRepository
import com.suchelin.shared.data.repository.GitLiveMenuRepository
import com.suchelin.shared.data.repository.GitLivePostRepository
import com.suchelin.shared.data.repository.GitLiveStoreRepository
import com.suchelin.shared.data.repository.GitLiveVoteRepository
import com.suchelin.shared.data.repository.MenuRepository
import com.suchelin.shared.data.repository.PostRepository
import com.suchelin.shared.data.repository.StoreRepository
import com.suchelin.shared.data.repository.VoteRepository
import org.koin.core.module.Module
import org.koin.dsl.module

private class AndroidDailyLimitRepository(context: Context) : DailyLimitRepository {
    private val prefs = context.getSharedPreferences("daily_limit", Context.MODE_PRIVATE)

    override suspend fun canPostToday(): Boolean {
        val today = kotlinx.datetime.Clock.System.now().toString().substring(0, 10)
        return prefs.getString("post_date", "") != today
    }

    override suspend fun markPostUsed() {
        val today = kotlinx.datetime.Clock.System.now().toString().substring(0, 10)
        prefs.edit().putString("post_date", today).apply()
    }

    override suspend fun canVoteToday(storeId: Int): Boolean {
        val today = kotlinx.datetime.Clock.System.now().toString().substring(0, 10)
        val savedDate = prefs.getString("vote_date", "") ?: ""
        if (savedDate != today) return true
        val count = prefs.getInt("vote_count", 0)
        return count < 10
    }

    override suspend fun markVoteUsed(storeId: Int) {
        val today = kotlinx.datetime.Clock.System.now().toString().substring(0, 10)
        val savedDate = prefs.getString("vote_date", "") ?: ""
        val nextCount = if (savedDate == today) prefs.getInt("vote_count", 0) + 1 else 1
        prefs.edit()
            .putString("vote_date", today)
            .putInt("vote_count", nextCount)
            .apply()
    }
}

actual val repositoryModule: Module = module {
    single<StoreRepository> { GitLiveStoreRepository() }
    single<MenuRepository> { GitLiveMenuRepository() }
    single<PostRepository> { GitLivePostRepository() }
    single<VoteRepository> { GitLiveVoteRepository() }
    single<AuthRepository> { GitLiveAuthRepository() }
    single<DailyLimitRepository> { AndroidDailyLimitRepository(get()) }
}
