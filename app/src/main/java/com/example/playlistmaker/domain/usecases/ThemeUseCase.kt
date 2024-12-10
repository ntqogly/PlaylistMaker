package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.ThemeRepository

class ThemeUseCase(private val themeRepository: ThemeRepository) {

    fun isDarkMode(): Boolean {
        return themeRepository.isDarkMode()
    }

    fun setDarkMode(enabled: Boolean) {
        themeRepository.setDarkMode(enabled)
    }
}
