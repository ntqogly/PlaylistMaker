package com.example.playlistmaker.data.repository

import com.example.playlistmaker.data.db.PlaylistDao
import com.example.playlistmaker.data.db.PlaylistEntity
import com.example.playlistmaker.data.db.PlaylistTrackDao
import com.example.playlistmaker.data.db.PlaylistTrackEntity
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.api.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistTrack
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlaylistRepositoryImpl(
    private val playlistDao: PlaylistDao,
    private val playlistTrackDao: PlaylistTrackDao,
    private val trackMapper: TrackMapper
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

    override suspend fun addTrackToPlaylist(playlistId: Long, trackId: String) {
        val existingTrackIdsJson = playlistDao.getTrackIdsForPlaylist(playlistId) ?: "[]"
        val trackIdsList =
            gson.fromJson(existingTrackIdsJson, Array<String>::class.java).toMutableList()
        if (!trackIdsList.contains(trackId)) {
            trackIdsList.add(trackId)
            playlistDao.updatePlaylistTracks(
                playlistId, gson.toJson(trackIdsList), trackIdsList.size
            )
            val updatedTrackIdsJson = playlistDao.getTrackIdsForPlaylist(playlistId) ?: "[]"
            val updatedTrackIds =
                gson.fromJson(updatedTrackIdsJson, Array<String>::class.java).toList()
        }
    }

    override suspend fun addTrackToDatabase(track: PlaylistTrack) {
        val trackEntity = trackMapper.mapToPlaylistEntity(track)
        playlistTrackDao.insertTrack(trackEntity)
    }

    override suspend fun isTrackInPlaylistDB(trackId: Long, playlistId: Long): Boolean {
        val tracks = playlistTrackDao.getTracksForPlaylist(playlistId)
        return tracks.any { it.trackId == trackId }
    }

}