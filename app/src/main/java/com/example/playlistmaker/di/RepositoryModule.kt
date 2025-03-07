package com.example.playlistmaker.di

import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.data.repository.FavoriteTrackRepositoryImpl
import com.example.playlistmaker.data.repository.PlaylistRepositoryImpl
import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.domain.api.PlaylistRepository
import com.example.playlistmaker.domain.api.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> { TrackRepositoryImpl(get(), get(), get(), get()) }

    single<FavoriteTrackRepository> { FavoriteTrackRepositoryImpl(get(), get()) }

    single<PlaylistRepository> { PlaylistRepositoryImpl(get(), get(), get()) }
}