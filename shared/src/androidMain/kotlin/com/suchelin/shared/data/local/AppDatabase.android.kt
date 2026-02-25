package com.suchelin.shared.data.local

import androidx.room.Room
import androidx.room.RoomDatabase
import com.suchelin.shared.di.getPlatformContext

actual fun getDatabaseBuilder(): RoomDatabase.Builder<AppDatabase> {
    return Room.databaseBuilder(getPlatformContext(), AppDatabase::class.java, "app.db")
}
