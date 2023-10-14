package com.suchelin.android.util.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StoreDataArgs(
    val storeId: Int,
    val storeName: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double
): Parcelable
