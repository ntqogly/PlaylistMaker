package com.example.playlistmaker.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlists ORDER BY id DESC")
    fun getAllPlaylists(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id = :playlistId")
    suspend fun getPlaylistById(playlistId: Long): PlaylistEntity?

    @Delete
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("UPDATE playlists SET trackIds = :trackIds, trackCount = :trackCount WHERE id = :playlistId")
    suspend fun updatePlaylistTracks(playlistId: Long, trackIds: String, trackCount: Int)
}