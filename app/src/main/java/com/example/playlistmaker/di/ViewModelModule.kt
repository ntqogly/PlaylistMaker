package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.media.favorite.FavoriteViewModel
import com.example.playlistmaker.presentation.media.playlist.newplaylist.CreatePlaylistViewModel
import com.example.playlistmaker.presentation.media.playlist.newplaylist.EditPlaylistViewModel
import com.example.playlistmaker.presentation.media.playlist.playlistdetails.PlaylistDetailsViewModel
import com.example.playlistmaker.presentation.media.playlist.playlist.PlaylistViewModel
import com.example.playlistmaker.presentation.player.PlayerViewModel
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { PlayerViewModel(get(), get(), get(), get()) }
    viewModel { SearchViewModel(get(), get()) }
    viewModel { SettingsViewModel(get()) }
    viewModel { PlaylistViewModel((get())) }
    viewModel { FavoriteViewModel(get()) }
    viewModel { CreatePlaylistViewModel(get()) }
    viewModel { PlaylistDetailsViewModel(get()) }
    viewModel { EditPlaylistViewModel(get()) }
}