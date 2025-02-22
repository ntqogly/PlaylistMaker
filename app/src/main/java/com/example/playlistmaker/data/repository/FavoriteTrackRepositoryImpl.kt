package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.FavoriteTrackEntity
import com.example.playlistmaker.domain.api.FavoriteTrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteTrackRepositoryImpl(
    private val appDatabase: AppDatabase
) : FavoriteTrackRepository {

    override suspend fun addToFavorites(track: Track) {
        val entity = mapToEntity(track)
        appDatabase.favoriteTrackDao().insertTrack(entity)
    }

    override suspend fun removeFromFavorites(track: Track) {
        val entity = mapToEntity(track)
        appDatabase.favoriteTrackDao().deleteTrack(entity)
    }

    override fun getAllFavoriteTracks(): Flow<List<Track>> {
        return appDatabase.favoriteTrackDao().getAllFavoriteTracks()
            .map { entityList -> entityList.map { mapToDomain(it) } }
    }

    override suspend fun getFavoriteTrackIds(): List<Long> {
        return appDatabase.favoriteTrackDao().getFavoriteTrackIds()
    }

    override suspend fun isTrackFavorite(trackId: String): Boolean {
        return appDatabase.favoriteTrackDao().getFavoriteTrackIds().contains(trackId.toLong())
    }

    private fun mapToEntity(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            trackId = track.trackId.toLong(),
            trackName = track.trackName,
            artistName = track.artistName,
            trackAlbum = track.trackAlbum,
            releaseDate = track.releaseDate,
            genre = track.genre,
            trackCountry = track.trackCountry,
            trackTimeMillis = track.trackTimeMillis,
            previewUrl = track.previewUrl,
            artworkUrl100 = track.artworkUrl100
        )
    }

    private fun mapToDomain(entity: FavoriteTrackEntity): Track {
        return Track(
            trackId = entity.trackId.toInt(),
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackAlbum = entity.trackAlbum,
            releaseDate = entity.releaseDate,
            genre = entity.genre,
            trackCountry = entity.trackCountry,
            trackTimeMillis = entity.trackTimeMillis,
            previewUrl = entity.previewUrl,
            artworkUrl100 = entity.artworkUrl100
        )
    }
}
