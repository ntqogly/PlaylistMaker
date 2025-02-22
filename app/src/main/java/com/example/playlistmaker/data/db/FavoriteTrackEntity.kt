package com.example.playlistmaker.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_tracks")
data class FavoriteTrackEntity(
    @PrimaryKey val trackId: Long,
    @ColumnInfo(name = "track_name") val trackName: String,
    @ColumnInfo(name = "artist_name") val artistName: String,
    @ColumnInfo(name = "album_name") val trackAlbum: String,
    @ColumnInfo(name = "release_year") val releaseDate: String?,
    @ColumnInfo(name = "genre") val genre: String,
    @ColumnInfo(name = "country") val trackCountry: String,
    @ColumnInfo(name = "track_time") val trackTimeMillis: Int,
    @ColumnInfo(name = "preview_url") val previewUrl: String?,
    @ColumnInfo(name = "artwork_url") val artworkUrl100: String
)
