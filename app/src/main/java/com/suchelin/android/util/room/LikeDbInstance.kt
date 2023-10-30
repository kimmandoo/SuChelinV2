package com.suchelin.android.util.room

import android.content.Context
import androidx.room.Room
import com.suchelin.android.util.room.database.LikeDB

object LikeDbInstance {
    private var instance: LikeDB? = null

    fun getDatabase(context: Context): LikeDB {
        if (instance == null) {
            synchronized(LikeDB::class.java) {
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        LikeDB::class.java, "likeDbVer2"
                    ).build()
                }
            }
        }
        return instance!!
    }
}