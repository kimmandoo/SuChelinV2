package com.suchelin.shared.data.local

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.RoomDatabase

@Entity(tableName = "feed")
data class FeedEntity(
    @PrimaryKey val date: String,
)

@Entity(tableName = "like")
data class LikeEntity(
    @PrimaryKey val key: String,
)

@Dao
interface FeedDao {
    @Query("SELECT * FROM feed WHERE date = :date")
    suspend fun findByDate(date: String): FeedEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: FeedEntity)
}

@Dao
interface LikeDao {
    @Query("SELECT * FROM like WHERE key = :key")
    suspend fun findByKey(key: String): LikeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: LikeEntity)
}

@Database(entities = [FeedEntity::class, LikeEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun feedDao(): FeedDao
    abstract fun likeDao(): LikeDao
}

expect fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase>
