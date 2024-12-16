package com.example.playlistmaker.di

import com.example.playlistmaker.domain.usecases.PlaybackInteractor
import com.example.playlistmaker.domain.usecases.SearchHistoryUseCase
import com.example.playlistmaker.domain.usecases.SearchTrackUseCase
import com.example.playlistmaker.domain.usecases.SupportInteractor
import com.example.playlistmaker.domain.usecases.ThemeUseCase
import org.koin.dsl.module

val interactorModule = module {
    single {
        SearchTrackUseCase(get(), get())
    }
    single {
        PlaybackInteractor(get())
    }
    single {
        SearchHistoryUseCase(get())
    }
    single {
        ThemeUseCase(get())
    }
    single {
        SupportInteractor(get())
    }
}
