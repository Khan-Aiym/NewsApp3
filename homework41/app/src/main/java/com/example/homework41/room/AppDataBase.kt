package com.example.homework41.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.homework41.Model.Model

@Database(entities = [Model::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}