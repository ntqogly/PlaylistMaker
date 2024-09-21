package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        applyTheme()
    }

    private fun applyTheme() {
        val sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE)
        if (!sharedPreferences.contains("is_dark_mode")) {
            val isSystemDarkMode =
                (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES
            sharedPreferences.edit().putBoolean("is_dark_mode", isSystemDarkMode).apply()
        }
        val isDarkMode = sharedPreferences.getBoolean("is_dark_mode", false)
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}