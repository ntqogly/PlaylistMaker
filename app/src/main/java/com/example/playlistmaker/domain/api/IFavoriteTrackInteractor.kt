package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface IFavoriteTrackInteractor {
    fun getFavoriteTracks(): Flow<List<Track>>

    suspend fun addTrackToFavorites(track: Track)

    suspend fun removeTrackFromFavorites(track: Track)

    suspend fun isTrackFavorite(trackId: String): Boolean
}