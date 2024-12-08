package com.example.playlistmaker

import SearchTrackUseCase
import android.content.Context
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.NetworkClientImpl
import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.data.sharedprefs.SearchHistory
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.usecases.SearchHistoryUseCase
import com.example.playlistmaker.domain.usecases.SupportInteractor

object Creator {
    private fun getTrackRepository(): TrackRepository {
        return TrackRepositoryImpl(getNetworkClient())
    }

    private fun getNetworkClient(): NetworkClient {
        return NetworkClientImpl()
    }

    fun provideSearchTrackUseCase(): SearchTrackUseCase {
        return SearchTrackUseCase(getTrackRepository())
    }

    fun provideSearchHistoryUseCase(context: Context): SearchHistoryUseCase {
        val sharedPreferences =
            context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        return SearchHistoryUseCase(SearchHistory(sharedPreferences))
    }

    fun provideSupportInteractor(context: Context): SupportInteractor {
        return SupportInteractor(context)
    }
}
