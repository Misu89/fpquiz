package com.example.fpquiz.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PartidaEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun partidaDao(): PartidaDao
}