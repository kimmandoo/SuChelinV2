package com.suchelin.shared.di

import com.suchelin.shared.data.repository.AuthRepository
import com.suchelin.shared.data.repository.DailyLimitRepository
import com.suchelin.shared.data.repository.MenuRepository
import com.suchelin.shared.data.repository.PostRepository
import com.suchelin.shared.data.repository.StoreRepository
import com.suchelin.shared.data.repository.VoteRepository
import com.suchelin.shared.viewmodel.FeedViewModel
import com.suchelin.shared.viewmodel.MainViewModel
import com.suchelin.shared.viewmodel.VoteViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

val sharedModule = module {
    single { MainViewModel(get(), get(), get()) }
    single { FeedViewModel(get(), get()) }
    single { VoteViewModel(get(), get()) }
}

expect val platformModule: org.koin.core.module.Module
expect val repositoryModule: org.koin.core.module.Module

fun initKoin(): KoinApplication = startKoin {
    modules(sharedModule, platformModule, repositoryModule)
}

fun doInitKoin() {
    initKoin()
}
