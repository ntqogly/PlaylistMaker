package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.domain.api.IFavoriteTrackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractor(
    private val favoriteTrackRepository: FavoriteTrackRepository
) : IFavoriteTrackInteractor {

    override fun getFavoriteTracks(): Flow<List<Track>> {
        return favoriteTrackRepository.getAllFavoriteTracks()
    }

    override suspend fun addTrackToFavorites(track: Track) {
        favoriteTrackRepository.addToFavorites(track)
    }

    override suspend fun removeTrackFromFavorites(track: Track) {
        favoriteTrackRepository.removeFromFavorites(track)
    }

    override suspend fun isTrackFavorite(trackId: String): Boolean {
        return favoriteTrackRepository.isTrackFavorite(trackId)
    }

}