package com.suchelin.android.util.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suchelin.android.util.room.dao.FeedDao
import com.suchelin.android.util.room.dto.FeedData

@Database(entities = [FeedData::class], version = 1)
abstract class FeedDB : RoomDatabase() {
    abstract fun feedDao(): FeedDao
}