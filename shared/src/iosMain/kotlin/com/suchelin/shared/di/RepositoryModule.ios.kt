package com.suchelin.shared.di

import com.suchelin.shared.data.repository.AuthRepository
import com.suchelin.shared.data.repository.DailyLimitRepository
import com.suchelin.shared.data.repository.GitLiveAuthRepository
import com.suchelin.shared.data.repository.GitLiveMenuRepository
import com.suchelin.shared.data.repository.GitLivePostRepository
import com.suchelin.shared.data.repository.GitLiveStoreRepository
import com.suchelin.shared.data.repository.GitLiveReportRepository
import com.suchelin.shared.data.repository.GitLiveVoteRepository
import com.suchelin.shared.data.repository.MenuRepository
import com.suchelin.shared.data.repository.ReportRepository
import com.suchelin.shared.data.repository.PostRepository
import com.suchelin.shared.data.repository.StoreRepository
import com.suchelin.shared.data.repository.VoteRepository
import kotlinx.datetime.Clock
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults

private class IosDailyLimitRepository : DailyLimitRepository {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    private fun today(): String = Clock.System.now().toString().substring(0, 10)

    override suspend fun canPostToday(): Boolean {
        val postDate = userDefaults.stringForKey("post_date") ?: ""
        return postDate != today()
    }

    override suspend fun markPostUsed() {
        userDefaults.setObject(today(), forKey = "post_date")
    }

    override suspend fun canVoteToday(storeId: Int): Boolean {
        val voteDate = userDefaults.stringForKey("vote_date") ?: ""
        if (voteDate != today()) return true
        val count = userDefaults.integerForKey("vote_count").toInt()
        return count < 10
    }

    override suspend fun markVoteUsed(storeId: Int) {
        val savedDate = userDefaults.stringForKey("vote_date") ?: ""
        val current = userDefaults.integerForKey("vote_count").toInt()
        val next = if (savedDate == today()) current + 1 else 1
        userDefaults.setObject(today(), forKey = "vote_date")
        userDefaults.setInteger(next.toLong(), forKey = "vote_count")
    }
}

actual val repositoryModule: Module = module {
    single<StoreRepository> { GitLiveStoreRepository() }
    single<MenuRepository> { GitLiveMenuRepository() }
    single<PostRepository> { GitLivePostRepository() }
    single<VoteRepository> { GitLiveVoteRepository() }
    single<AuthRepository> { GitLiveAuthRepository() }
    single<DailyLimitRepository> { IosDailyLimitRepository() }
    single<ReportRepository> { GitLiveReportRepository() }
}
