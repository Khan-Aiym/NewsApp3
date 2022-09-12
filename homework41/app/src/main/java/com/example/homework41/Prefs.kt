package com.example.homework41

import android.content.Context
import android.content.SharedPreferences

class Prefs constructor(context: Context) {
    private val preferences: SharedPreferences
    fun saveBoardState() {
        preferences.edit().putBoolean("avatar", true).apply()
    }

    val isBoardShown: Boolean
        get() {
            return preferences.getBoolean("avatar", false)
        }

    fun saveAvatar(image: String?) {
        preferences.edit().putString("image", image).apply()
    }

    val avatar: String?
        get() {
            return preferences.getString("image", null)
        }

    fun saveName(name: String?) {
        preferences.edit().putString("name", name).apply()
    }

    val name: String?
        get() {
            return preferences.getString("name", "")
        }

    fun cleanPreferences() {
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.remove("name").apply()
        editor.remove("image").apply()
    }

    init {
        preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    }
}