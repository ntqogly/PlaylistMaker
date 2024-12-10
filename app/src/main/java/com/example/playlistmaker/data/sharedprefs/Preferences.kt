package com.example.playlistmaker.data.sharedprefs

import android.content.SharedPreferences

class Preferences(private val sharedPreferences: SharedPreferences) {

    companion object {
        fun create(sharedPreferences: SharedPreferences): Preferences {
            return Preferences(sharedPreferences)
        }
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        val value = sharedPreferences.getBoolean(key, defaultValue)
        return value
    }

    fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }
}