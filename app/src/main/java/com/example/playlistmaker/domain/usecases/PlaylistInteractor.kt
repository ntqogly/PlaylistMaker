package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

class PlaylistInteractor(private val repository: PlaylistRepository) {

    suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }

    fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    suspend fun addTrackToPlaylist(playlistId: Long, track: PlaylistTrack) {
        repository.addTrackToDatabase(track)
        repository.addTrackToPlaylist(playlistId, track.trackId)
    }

    suspend fun isTrackInPlaylistDB(trackId: Long, playlistId: Long): Boolean {
        return repository.isTrackInPlaylistDB(trackId, playlistId)
    }

    fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return repository.getPlaylistById(playlistId)
    }

    fun getTracksForPlaylist(playlistId: Long): Flow<List<PlaylistTrack>> {
        return repository.getTracksForPlaylist(playlistId)
    }

    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long) {
        repository.deleteTrackFromPlaylist(trackId, playlistId)
    }

    suspend fun deletePlaylist(playlistId: Long) {
        repository.deletePlaylist(playlistId)
    }
}