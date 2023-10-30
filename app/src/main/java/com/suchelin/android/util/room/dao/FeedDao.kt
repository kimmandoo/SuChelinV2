package com.suchelin.android.util.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.suchelin.android.util.room.dto.FeedData

@Dao
interface FeedDao {
    @Query("SELECT * FROM feeddata")
    fun getAll(): List<FeedData>

    @Insert
    fun insertAll(vararg feeds: FeedData)
}