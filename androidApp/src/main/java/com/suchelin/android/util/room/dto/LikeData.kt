package com.suchelin.android.util.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class LikeData(
    @PrimaryKey val date: String,
    @ColumnInfo(name = "like") val like: Int?
)