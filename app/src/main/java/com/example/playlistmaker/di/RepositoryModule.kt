package com.example.playlistmaker.di

import MediaPlayerRepository
import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.api.TrackRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<TrackRepository> { TrackRepositoryImpl(get(), get()) }
}