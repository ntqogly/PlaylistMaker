package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.data.sharedprefs.SearchHistory
import com.example.playlistmaker.domain.models.Track

class SearchHistoryUseCase(private val searchHistory: SearchHistory) {
    fun addTrackToHistory(track: Track) {
        searchHistory.addTrack(track)
    }

    fun getSearchHistory(): List<Track> {
        return searchHistory.getHistory()
    }

    fun clearHistory() {
        searchHistory.clearHistory()
    }
}