package com.suchelin.android.util.room.dto

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class FeedData(
    @PrimaryKey val date: String,
    @ColumnInfo(name = "feed") val feed: Boolean?
)