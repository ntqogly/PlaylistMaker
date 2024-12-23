package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

data class TrackDTO(
    @SerializedName("trackId") val trackId: Int,
    @SerializedName("trackName") val trackName: String,
    @SerializedName("artistName") val artistName: String,
    @SerializedName("trackTimeMillis") val trackTimeMillis: Int,
    @SerializedName("artworkUrl100") val artworkUrl100: String,
    @SerializedName("country") val trackCountry: String,
    @SerializedName("collectionName") val trackAlbum: String,
    @SerializedName("primaryGenreName") val genre: String,
    @SerializedName("releaseDate") val releaseDate: String,
    @SerializedName("previewUrl") val previewUrl: String
)