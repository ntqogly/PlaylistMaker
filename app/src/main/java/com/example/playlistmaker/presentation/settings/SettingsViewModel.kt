package com.example.playlistmaker.presentation.settings

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
        themeUseCase.applySavedTheme()
        _isDarkMode.value = isEnabled
    }
}
