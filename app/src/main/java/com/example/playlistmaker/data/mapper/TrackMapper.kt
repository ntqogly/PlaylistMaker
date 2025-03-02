package com.example.playlistmaker.data.mapper

import android.util.Log
import com.example.playlistmaker.data.db.FavoriteTrackEntity
import com.example.playlistmaker.data.db.PlaylistTrackEntity
import com.example.playlistmaker.data.dto.TrackDTO
import com.example.playlistmaker.domain.models.PlaylistTrack
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
            previewUrl = dto.previewUrl,
            isFavorite = false
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

    fun mapFromEntity(entity: FavoriteTrackEntity): Track {
        return Track(
            trackId = entity.trackId.toInt(),
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackTimeMillis = entity.trackTimeMillis,
            artworkUrl100 = entity.artworkUrl100,
            trackCountry = entity.trackCountry,
            trackAlbum = entity.trackAlbum,
            genre = entity.genre,
            releaseDate = entity.releaseDate,
            previewUrl = entity.previewUrl,
            isFavorite = true
        )
    }

    fun mapToEntity(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            trackId = track.trackId.toLong(),
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackCountry = track.trackCountry,
            trackAlbum = track.trackAlbum,
            genre = track.genre,
            releaseDate = track.releaseDate,
            previewUrl = track.previewUrl
        )
    }

    fun mapFromPlaylistEntity(entity: PlaylistTrackEntity): PlaylistTrack {
        return PlaylistTrack(
            trackId = entity.trackId.toString(),
            playlistId = entity.playlistId,
            trackName = entity.trackName,
            artistName = entity.artistName,
            trackTimeMillis = entity.trackTimeMillis,
            artworkUrl100 = entity.artworkUrl100,
            trackCountry = entity.trackCountry,
            trackAlbum = entity.trackAlbum,
            genre = entity.genre,
            releaseDate = entity.releaseDate,
            previewUrl = entity.previewUrl
        )
    }

    // ✅ Новый метод: конвертация PlaylistTrack → PlaylistTrackEntity
    fun mapToPlaylistEntity(track: PlaylistTrack): PlaylistTrackEntity {
        return PlaylistTrackEntity(
            trackId = track.trackId.toLong(),
            playlistId = track.playlistId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTimeMillis = track.trackTimeMillis,
            artworkUrl100 = track.artworkUrl100,
            trackCountry = track.trackCountry,
            trackAlbum = track.trackAlbum ?: "",
            genre = track.genre,
            releaseDate = track.releaseDate ?: "",
            previewUrl = track.previewUrl
        )
    }

}