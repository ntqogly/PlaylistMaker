package com.example.playlistmaker.data.repository

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeRepositoryImpl(private val sharedPreferences: SharedPreferences) : ThemeRepository {

    override fun isDarkMode(): Boolean {
        return sharedPreferences.getBoolean("is_dark_mode", false)
    }

    override fun setDarkMode(enabled: Boolean) {
        sharedPreferences.edit().putBoolean("is_dark_mode", enabled).apply()
    }

    override fun applyTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
