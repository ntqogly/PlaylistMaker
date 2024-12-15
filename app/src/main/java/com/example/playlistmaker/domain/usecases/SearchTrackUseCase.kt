package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.ISearchTrackUseCase
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.threads.ThreadExecutor

class SearchTrackUseCase(
    private val trackRepository: TrackRepository, private val threadExecutor: ThreadExecutor
) : ISearchTrackUseCase {

    override fun executeAsync(expression: String, callback: (tracks: List<Track>) -> Unit) {
        threadExecutor.executeInBackground {
            try {
                val tracks = trackRepository.searchTrack(expression)
                threadExecutor.postToMainThread {
                    callback(tracks)
                }
            } catch (e: Exception) {
                threadExecutor.postToMainThread {
                    callback(emptyList())
                }
            }
        }
    }
}

