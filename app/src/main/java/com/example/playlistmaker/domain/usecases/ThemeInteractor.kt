package com.example.playlistmaker.domain.usecases

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeInteractor(private val themeRepository: ThemeRepository) {

    fun isDarkMode(): Boolean {
        return themeRepository.isDarkMode()
    }

    fun setDarkMode(isEnabled: Boolean) {
        themeRepository.setDarkMode(isEnabled)
    }

    fun applySavedTheme() {
        val isDarkMode = themeRepository.isDarkMode()
        val mode = if (isDarkMode) {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}

