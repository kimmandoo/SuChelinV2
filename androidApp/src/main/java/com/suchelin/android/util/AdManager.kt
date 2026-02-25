package com.suchelin.android.util

import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

object AdManager {
    fun initialize(context: Context) {
        MobileAds.initialize(context)
    }

    fun createAdRequest(): AdRequest {
        return AdRequest.Builder().build()
    }
}