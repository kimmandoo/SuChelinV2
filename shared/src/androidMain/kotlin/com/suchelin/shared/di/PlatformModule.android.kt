package com.suchelin.shared.di

import android.content.Context
import org.koin.core.module.Module
import org.koin.dsl.module

private lateinit var appContext: Context

fun setPlatformContext(context: Context) {
    appContext = context.applicationContext
}

fun getPlatformContext(): Context = appContext

actual val platformModule: Module = module {
    single<Context> { getPlatformContext() }
}
