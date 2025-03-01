package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylistTracks(playlistId: Long, trackIds: List<String>)
    suspend fun isTrackInPlaylist(playlistId: Long, trackId: String): Boolean
    suspend fun addTrackToPlaylist(playlistId: Long, trackId: String)

}