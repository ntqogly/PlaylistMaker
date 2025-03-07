package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase, private val trackMapper: TrackMapper
) : FavoriteTrackRepository {

    override suspend fun addToFavorites(track: Track) {
        val entity = trackMapper.mapToEntity(track)
        appDatabase.favoriteTrackDao().insertTrack(entity)
    }

    override suspend fun removeFromFavorites(track: Track) {
        val entity = trackMapper.mapToEntity(track)
        appDatabase.favoriteTrackDao().deleteTrack(entity)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> {
        return appDatabase.favoriteTrackDao().getAllFavoriteTracks()
            .map { entityList -> entityList.map { trackMapper.mapFromEntity(it) } }
    }

    override suspend fun getFavoriteTrackIds(): List<Long> {
        return appDatabase.favoriteTrackDao().getFavoriteTrackIds()
    }

    override suspend fun isTrackFavorite(trackId: String): Boolean {
        return appDatabase.favoriteTrackDao().getFavoriteTrackIds().contains(trackId.toLong())
    }
}
