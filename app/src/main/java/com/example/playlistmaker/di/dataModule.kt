package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.data.network.ITunesApiService
import com.example.playlistmaker.data.network.NetworkClient
import com.example.playlistmaker.data.network.NetworkClientImpl
import com.example.playlistmaker.data.repository.SharedPreferencesSearchHistory
import com.example.playlistmaker.data.repository.ThemeRepositoryImpl
import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.api.ThemeRepository
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.core.scope.get
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        Gson()
    }
    single {
        androidContext().getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    }
    single<ThemeRepository> {
        ThemeRepositoryImpl(get())
    }
    single<SearchHistoryRepository> {
        SharedPreferencesSearchHistory(get())
    }
    single<NetworkClient> {
        NetworkClientImpl(get(), androidContext())
    }
    single<ITunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesApiService::class.java)
    }
    single {
        TrackMapper()
    }
}
