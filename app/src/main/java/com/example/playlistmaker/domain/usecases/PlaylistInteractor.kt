package com.example.playlistmaker.domain.usecases

import android.util.Log
import com.example.playlistmaker.data.db.PlaylistTrackEntity
import com.example.playlistmaker.domain.api.PlaylistRepository
import com.example.playlistmaker.domain.models.Playlist
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

    suspend fun isTrackInPlaylist(playlistId: Long, trackId: String): Boolean {
        return repository.isTrackInPlaylist(playlistId, trackId)
    }

    suspend fun addTrackToPlaylist(playlistId: Long, track: PlaylistTrackEntity) {
        repository.addTrackToDatabase(track)
        repository.addTrackToPlaylist(playlistId, track.trackId.toString())
    }
}