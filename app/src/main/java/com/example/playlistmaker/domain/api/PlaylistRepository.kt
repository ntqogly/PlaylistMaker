package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistTrack
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {
    suspend fun createPlaylist(playlist: Playlist)
    fun getAllPlaylists(): Flow<List<Playlist>>
    suspend fun deletePlaylist(playlist: Playlist)
    suspend fun updatePlaylistTracks(playlistId: Long, trackIds: List<String>)
    suspend fun addTrackToPlaylist(playlistId: Long, trackId: String)
    suspend fun addTrackToDatabase(track: PlaylistTrack)
    suspend fun isTrackInPlaylistDB(trackId: Long, playlistId: Long): Boolean
    fun getPlaylistById(playlistId: Long): Flow<Playlist>
    fun getTracksForPlaylist(playlistId: Long): Flow<List<PlaylistTrack>>
    suspend fun deleteTrackFromPlaylist(trackId: Long, playlistId: Long)
    suspend fun removeTrackIfUnused(trackId: Long)
}