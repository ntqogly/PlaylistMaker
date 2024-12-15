package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.api.MediaPlayerRepository

class PlaybackInteractor(private val mediaPlayerRepository: MediaPlayerRepository) :
    IPlaybackInteractor {

    override fun setup(url: String, onComplete: () -> Unit) {
        mediaPlayerRepository.setup(url, onComplete)
    }

    override fun play(onComplete: () -> Unit) {
        mediaPlayerRepository.play(onComplete)
    }

    override fun pause(onComplete: () -> Unit) {
        mediaPlayerRepository.pause(onComplete)
    }

    override fun stop() {
        mediaPlayerRepository.stop()
    }

    override fun isPlaying(): Boolean {
        return mediaPlayerRepository.isPlaying()
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayerRepository.getCurrentPosition()
    }

    override fun togglePlayback(onPlay: () -> Unit, onPause: () -> Unit) {
        if (mediaPlayerRepository.isPlaying()) {
            mediaPlayerRepository.pause(onPause)
        } else {
            mediaPlayerRepository.play(onPlay)
        }
    }

    override fun getCurrentTimeFormatted(): String {
        val currentPosition = getCurrentPosition() / 1000
        val minutes = currentPosition / 60
        val seconds = currentPosition % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
