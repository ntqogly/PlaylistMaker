package com.example.playlistmaker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val description: String?,
    val coverPath: String?,  // Путь к обложке
    val trackIds: String = "[]",  // JSON-список идентификаторов треков
    val trackCount: Int = 0
) {}