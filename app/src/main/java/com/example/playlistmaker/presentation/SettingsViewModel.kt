package com.example.playlistmaker.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecases.ThemeUseCase

class SettingsViewModel(private val themeUseCase: ThemeUseCase) : ViewModel() {

    private val _isDarkMode = MutableLiveData<Boolean>()
    val isDarkMode: LiveData<Boolean> get() = _isDarkMode

    init {
        _isDarkMode.value = themeUseCase.isDarkMode()
    }

    fun toggleTheme(isEnabled: Boolean) {
        themeUseCase.setDarkMode(isEnabled)
        _isDarkMode.value = isEnabled
    }

    fun applySavedTheme() {
        themeUseCase.applySavedTheme()
    }
}