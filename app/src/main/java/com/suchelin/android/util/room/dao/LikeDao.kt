package com.suchelin.android.util.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.suchelin.android.util.room.dto.LikeData

@Dao
interface LikeDao {
    @Query("SELECT * FROM likedata")
    fun getAll(): List<LikeData>

    @Insert
    fun insertAll(vararg likes: LikeData)
}