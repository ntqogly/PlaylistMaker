package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackRepository {
    suspend fun addToFavorites(track: Track)
    suspend fun removeFromFavorites(track: Track)
    fun getAllFavoriteTracks(): Flow<List<Track>>
    suspend fun getFavoriteTrackIds(): List<Long>
    suspend fun isTrackFavorite(trackId: String): Boolean
}
