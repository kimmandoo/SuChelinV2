package com.suchelin.android.v2.util.room

import android.content.Context
import androidx.room.Room
import com.suchelin.android.v2.util.room.database.FeedDB

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