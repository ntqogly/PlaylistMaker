package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface ISearchTrackUseCase {
    suspend fun execute(expression: String): Flow<List<Track>>
}