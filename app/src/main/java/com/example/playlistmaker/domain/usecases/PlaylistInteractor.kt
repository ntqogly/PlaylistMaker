package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.data.db.PlaylistTrackEntity
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

    suspend fun deletePlaylist(playlist: Playlist) {
        repository.deletePlaylist(playlist)
    }

    suspend fun updatePlaylistTracks(playlistId: Long, trackIds: List<String>) {
        repository.updatePlaylistTracks(playlistId, trackIds)
    }

    suspend fun addTrackToPlaylist(playlistId: Long, track: PlaylistTrack) {
        repository.addTrackToDatabase(track)
        repository.addTrackToPlaylist(playlistId, track.trackId)
    }

    suspend fun isTrackInPlaylistDB(trackId: Long, playlistId: Long): Boolean {
        return repository.isTrackInPlaylistDB(trackId, playlistId)
    }
}