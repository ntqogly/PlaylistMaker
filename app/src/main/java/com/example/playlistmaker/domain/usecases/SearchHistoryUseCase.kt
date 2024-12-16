package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.SearchHistoryRepository
import com.example.playlistmaker.domain.models.Track

class SearchHistoryUseCase(private val searchHistoryRepository: SearchHistoryRepository) {

    fun addTrackToHistory(track: Track) {
        searchHistoryRepository.addTrack(track)
    }

    fun getSearchHistory(): List<Track> {
        return searchHistoryRepository.getHistory()
    }

    fun clearHistory() {
        searchHistoryRepository.clearHistory()
    }
}
