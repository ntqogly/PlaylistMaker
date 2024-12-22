package com.example.playlistmaker.data.mapper

import android.util.Log
import com.example.playlistmaker.data.dto.TrackDTO
import com.example.playlistmaker.domain.models.Track

class TrackMapper {
    private fun mapToDomain(dto: TrackDTO): Track {
        return Track(
            trackId = dto.trackId,
            trackName = dto.trackName,
            artistName = dto.artistName,
            trackTimeMillis = dto.trackTimeMillis,
            artworkUrl100 = dto.artworkUrl100,
            trackCountry = dto.trackCountry,
            trackAlbum = dto.trackAlbum,
            genre = dto.genre,
            releaseDate = dto.releaseDate,
            previewUrl = dto.previewUrl
        )
    }

    fun mapToDomainList(dtoList: List<TrackDTO>): List<Track> {
        val mappedList = dtoList.mapNotNull { dto ->
            try {
                mapToDomain(dto)
            } catch (e: Exception) {
                Log.e("TrackMapperError", "Error mapping DTO: ${e.message}")
                null
            }
        }
        return mappedList
    }


}