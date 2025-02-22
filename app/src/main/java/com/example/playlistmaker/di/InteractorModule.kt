package com.example.playlistmaker.di

import MediaPlayerRepository
import com.example.playlistmaker.domain.api.IFavoriteTrackInteractor
import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.api.ISearchTrackUseCase
import com.example.playlistmaker.domain.api.ISupportInteractor
import com.example.playlistmaker.domain.usecases.FavoriteTrackInteractor
import com.example.playlistmaker.domain.usecases.PlaybackInteractor
import com.example.playlistmaker.domain.usecases.SearchHistoryUseCase
import com.example.playlistmaker.domain.usecases.SearchTrackUseCase
import com.example.playlistmaker.domain.usecases.SupportInteractor
import com.example.playlistmaker.domain.usecases.ThemeInteractor
import org.koin.dsl.module

val interactorModule = module {
    single<ISearchTrackUseCase> { SearchTrackUseCase(get()) }
    single { PlaybackInteractor(get()) }
    single<IPlaybackInteractor> { MediaPlayerRepository(get()) }
    single<ISupportInteractor> { SupportInteractor(get()) }
    single { SearchHistoryUseCase(get()) }
    single { ThemeInteractor(get()) }

    single<IFavoriteTrackInteractor> { FavoriteTrackInteractor(get()) }
}
