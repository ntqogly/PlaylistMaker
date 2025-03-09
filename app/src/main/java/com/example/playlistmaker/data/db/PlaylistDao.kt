package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists ORDER BY id DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    fun observePlaylistById(playlistId: Long): Flow<PlaylistEntity>

    @Query("DELETE FROM playlists WHERE id = :playlistId")
    suspend fun deletePlaylistById(playlistId: Long)

    @Query("UPDATE playlists SET trackIds = :trackIds, trackCount = :trackCount WHERE id = :playlistId")
    suspend fun updatePlaylistTracks(playlistId: Long, trackIds: String, trackCount: Int)

    @Query("SELECT trackIds FROM playlists WHERE id = :playlistId")
    suspend fun getTrackIdsForPlaylist(playlistId: Long): String?

    suspend fun addTrackToPlaylist(playlistId: Long, trackId: String) {
        val existingTrackIdsJson = getTrackIdsForPlaylist(playlistId) ?: "[]"
        val trackIdsList =
            Gson().fromJson(existingTrackIdsJson, Array<String>::class.java).toMutableList()

        if (!trackIdsList.contains(trackId)) {
            trackIdsList.add(trackId)
            updatePlaylistTracks(playlistId, Gson().toJson(trackIdsList), trackIdsList.size)
        }
    }
}