package com.example.playlistmaker.domain.models

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String?,
    val coverPath: String?,  // Путь к обложке
    val trackIds: List<String> = emptyList()  // Список ID треков

)
