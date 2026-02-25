package com.suchelin.android.v2.util.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.suchelin.android.v2.util.room.dao.LikeDao
import com.suchelin.android.v2.util.room.dto.LikeData

@Database(entities = [LikeData::class], version = 1, exportSchema = false)
abstract class LikeDB : RoomDatabase() {
    abstract fun likeDao(): LikeDao
}
