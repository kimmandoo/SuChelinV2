package com.suchelin.android.v2.util.parcelable

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize

data class SingleMapDataArgs(
    val storeName: String,
    val latitude: Double,
    val longitude: Double
): Parcelable
