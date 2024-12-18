package com.example.playlistmaker.presentation.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.usecases.ThemeInteractor

class SettingsViewModel(private val themeInteractor: ThemeInteractor) : ViewModel() {

    private val _state = MutableLiveData<SettingsState>()
    val state: LiveData<SettingsState> get() = _state

    init {
        _state.value = SettingsState(isDarkMode = themeInteractor.isDarkMode())
    }

    fun toggleTheme(isEnabled: Boolean) {
        themeInteractor.setDarkMode(isEnabled)
        themeInteractor.applySavedTheme()
        _state.value = _state.value?.copy(isDarkMode = isEnabled)
    }
}
