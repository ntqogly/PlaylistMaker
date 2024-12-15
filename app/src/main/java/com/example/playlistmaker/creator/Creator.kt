package com.example.playlistmaker.creator

import AndroidMediaPlayerRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.data.network.NetworkClientImpl
import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.data.repository.ThemeRepositoryImpl
import com.example.playlistmaker.data.repository.SharedPreferencesSearchHistory
import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.api.ISearchTrackUseCase
import com.example.playlistmaker.domain.api.ISupportInteractor
import com.example.playlistmaker.domain.api.ThemeRepository
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.usecases.PlaybackInteractor
import com.example.playlistmaker.domain.usecases.SearchHistoryUseCase
import com.example.playlistmaker.domain.usecases.SearchTrackUseCase
import com.example.playlistmaker.domain.usecases.SupportInteractor
import com.example.playlistmaker.domain.usecases.ThemeUseCase
import com.example.playlistmaker.presentation.player.PlayerViewModel
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingsViewModel

object Creator {

    private val networkClient = NetworkClientImpl()
    private val trackMapper = TrackMapper()

    private val trackRepository: TrackRepository by lazy {
        TrackRepositoryImpl(networkClient, trackMapper)
    }

    fun provideSearchTrackUseCase(): ISearchTrackUseCase {
        return SearchTrackUseCase(trackRepository)
    }

    fun provideSearchHistoryUseCase(context: Context): SearchHistoryUseCase {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val searchHistoryRepository = SharedPreferencesSearchHistory(sharedPreferences)
        return SearchHistoryUseCase(searchHistoryRepository)
    }


    fun providePlaybackInteractor(): IPlaybackInteractor {
        return PlaybackInteractor(AndroidMediaPlayerRepository())
    }

    fun provideSupportInteractor(context: Context): ISupportInteractor {
        return SupportInteractor(context)
    }

    private fun provideThemeRepository(context: Context): ThemeRepository {
        val sharedPreferences = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return ThemeRepositoryImpl(sharedPreferences)
    }

    fun provideThemeUseCase(context: Context): ThemeUseCase {
        return ThemeUseCase(provideThemeRepository(context))
    }

    fun provideSearchViewModelFactory(context: Context): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                    return SearchViewModel(provideSearchTrackUseCase()) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    fun providePlayerViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
                    return PlayerViewModel(providePlaybackInteractor()) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }

    fun provideSettingsViewModelFactory(context: Context): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SettingsViewModel::class.java)) {
                    return SettingsViewModel(provideThemeUseCase(context)) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }
    }
}

