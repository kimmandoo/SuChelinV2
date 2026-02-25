package com.suchelin.android

import android.app.Application
import com.suchelin.shared.di.doInitKoin
import com.suchelin.shared.di.setPlatformContext

class SuChelinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        setPlatformContext(this)
        doInitKoin()
    }
}
