package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.db.PlaylistDao
import com.example.playlistmaker.data.db.PlaylistEntity
import com.example.playlistmaker.domain.api.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao
) : PlaylistRepository {
    private val gson = Gson()

    override suspend fun createPlaylist(playlist: Playlist) {
        val entity = PlaylistEntity(
            name = playlist.name,
            description = playlist.description,
            coverPath = playlist.coverPath,
            trackIds = gson.toJson(playlist.trackIds),
            trackCount = playlist.trackIds.size
        )
        playlistDao.insertPlaylist(entity)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return playlistDao.getAllPlaylists().map { entities ->
            entities.map { entity ->
                Playlist(
                    id = entity.id,
                    name = entity.name,
                    description = entity.description,
                    coverPath = entity.coverPath,
                    trackIds = gson.fromJson(entity.trackIds, Array<String>::class.java).toList()
                )
            }
        }
    }

    override suspend fun deletePlaylist(playlist: Playlist) {
        val entity = PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverPath = playlist.coverPath,
            trackIds = gson.toJson(playlist.trackIds),
            trackCount = playlist.trackIds.size
        )
        playlistDao.deletePlaylist(entity)
    }

    override suspend fun updatePlaylistTracks(playlistId: Long, trackIds: List<String>) {
        playlistDao.updatePlaylistTracks(playlistId, gson.toJson(trackIds), trackIds.size)
    }

    override suspend fun isTrackInPlaylist(playlistId: Long, trackId: String): Boolean {
        val existingTrackIdsJson = playlistDao.getTrackIdsForPlaylist(playlistId) ?: "[]"
        val trackIdsList = gson.fromJson(existingTrackIdsJson, Array<String>::class.java).toList()
        return trackIdsList.contains(trackId)
    }

    override suspend fun addTrackToPlaylist(playlistId: Long, trackId: String) {
        playlistDao.addTrackToPlaylist(playlistId, trackId)
    }

}