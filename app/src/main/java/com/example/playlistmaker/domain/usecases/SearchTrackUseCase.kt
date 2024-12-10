
package com.example.playlistmaker.domain.usecases

import android.os.Handler
import android.os.Looper
import com.example.playlistmaker.domain.api.ISearchTrackUseCase
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track

class SearchTrackUseCase(
    private val trackRepository: TrackRepository
) : ISearchTrackUseCase {

    private val mainThreadHandler = Handler(Looper.getMainLooper())

    override fun executeAsync(expression: String, callback: (tracks: List<Track>) -> Unit) {
        Thread {
            try {
                val tracks = trackRepository.searchTrack(expression)
                mainThreadHandler.post {
                    callback(tracks)
                }
            } catch (e: Exception) {
                mainThreadHandler.post {
                    callback(emptyList())
                }
            }
        }.start()
    }
}
