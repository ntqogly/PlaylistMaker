package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.ISearchTrackUseCase
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

class SearchTrackUseCase(
    private val trackRepository: TrackRepository
) : ISearchTrackUseCase {

    override suspend fun execute(expression: String): Flow<List<Track>> {
        return trackRepository.searchTrack(expression)
    }

}

