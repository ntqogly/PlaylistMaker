package com.example.playlistmaker.domain.usecases

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeUseCase(private val themeRepository: ThemeRepository) {

    fun isDarkMode(): Boolean {
        return themeRepository.isDarkMode()
    }

    fun setDarkMode(enabled: Boolean) {
        themeRepository.setDarkMode(enabled)
    }

    fun applySavedTheme() {
        val isDarkMode = themeRepository.isDarkMode()
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
