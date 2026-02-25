package com.suchelin.android.util.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.suchelin.android.util.room.dto.LikeData

@Dao
interface LikeDao {
    @Query("SELECT * FROM likedata")
    fun getAll(): List<LikeData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(like: LikeData)

    @Update
    fun update(like: LikeData)
}