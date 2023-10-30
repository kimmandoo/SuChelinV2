package com.suchelin.android.util.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suchelin.android.util.room.dao.LikeDao
import com.suchelin.android.util.room.dto.LikeData

@Database(entities = [LikeData::class], version = 1)
abstract class LikeDB : RoomDatabase() {
    abstract fun likeDao(): LikeDao
}