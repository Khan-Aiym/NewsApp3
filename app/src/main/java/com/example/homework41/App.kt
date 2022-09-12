package com.example.homework41

import android.app.Application
import androidx.room.Room
import com.example.homework41.room.AppDataBase

class App constructor() : Application() {
    override fun onCreate() {
        super.onCreate()
        dataBase =
            Room.databaseBuilder(this, AppDataBase::class.java, "database").allowMainThreadQueries()
                .build()
    }

    companion object {
        var dataBase: AppDataBase? = null
            private set
    }
}