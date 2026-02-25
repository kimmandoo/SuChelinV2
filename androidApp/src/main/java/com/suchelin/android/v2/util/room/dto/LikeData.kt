package com.suchelin.android.v2.util.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikeData(
    @PrimaryKey val date: String,
    @ColumnInfo(name = "like") val like: Int?
)