package com.suchelin.android.util.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.suchelin.android.util.room.dto.FeedData
import com.suchelin.android.util.room.dto.LikeData

@Dao
interface FeedDao {
    @Query("SELECT * FROM feeddata")
    fun getAll(): List<FeedData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(feeds: FeedData)

    @Update
    fun update(feeds: FeedData)

}