package com.suchelin.android.util.room

import android.content.Context
import androidx.room.Room
import com.suchelin.android.util.room.database.FeedDB
import com.suchelin.android.util.room.database.LikeDB

object FeedDbInstance {
    private var instance: FeedDB? = null

    fun getDatabase(context: Context): FeedDB {
        if (instance == null) {
            synchronized(FeedDB::class.java) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FeedDB::class.java, "feedDB"
                    ).build()
                }
            }
        }
        return instance!!
    }
}