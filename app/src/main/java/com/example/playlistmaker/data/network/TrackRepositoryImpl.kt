package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class TrackRepositoryImpl(
    private val networkClient: NetworkClient, private val trackMapper: TrackMapper
) : TrackRepository {

    override fun searchTrack(expression: String): List<Track> {
        val response = networkClient.searchTrack(TrackSearchRequest(expression))
        if (response.resultCode == 200 && response is TrackResponse) {
            return trackMapper.mapToDomainList(response.results)
        }
        return emptyList()
    }
}