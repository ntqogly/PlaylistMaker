package com.example.playlistmaker.domain.models

data class PlaylistTrack(
    val trackId: String,
    val playlistId: Long,
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackCountry: String,
    val trackAlbum: String?,
    val genre: String,
    val releaseDate: String?,
    val previewUrl: String?
)