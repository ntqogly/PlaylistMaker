package com.example.playlistmaker.domain.api

interface ThemeRepository {
    fun isDarkMode(): Boolean
    fun setDarkMode(enabled: Boolean)
    fun applyTheme(isDarkMode: Boolean)
}