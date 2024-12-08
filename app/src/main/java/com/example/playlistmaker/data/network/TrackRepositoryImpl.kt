package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.searchTrack(TrackSearchRequest(expression))
        if (response.resultCode == 200 && response is TrackResponse) {
            return response.results.map { trackDTO ->
                Track(
                    trackId = trackDTO.trackId,
                    trackName = trackDTO.trackName,
                    artistName = trackDTO.artistName,
                    trackTimeMillis = trackDTO.trackTimeMillis,
                    artworkUrl100 = trackDTO.artworkUrl100,
                    trackCountry = trackDTO.trackCountry,
                    trackAlbum = trackDTO.trackAlbum,
                    genre = trackDTO.genre,
                    releaseDate = trackDTO.releaseDate,
                    previewUrl = trackDTO.previewUrl
                )
            }
        }
        return emptyList()
    }
}





