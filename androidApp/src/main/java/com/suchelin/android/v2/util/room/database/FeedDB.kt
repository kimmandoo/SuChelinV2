package com.suchelin.android.v2.util.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suchelin.android.v2.util.room.dao.FeedDao
import com.suchelin.android.v2.util.room.dto.FeedData

@Database(entities = [FeedData::class], version = 1, exportSchema = false)
abstract class FeedDB : RoomDatabase() {
    abstract fun feedDao(): FeedDao
}