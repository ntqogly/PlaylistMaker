package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface ISearchTrackUseCase {
    fun executeAsync(expression: String, callback: (tracks: List<Track>) -> Unit)
}