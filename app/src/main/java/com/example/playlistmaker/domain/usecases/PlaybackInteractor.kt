package com.example.playlistmaker.domain.usecases

import android.media.MediaPlayer

class PlaybackInteractor {

    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying: Boolean = false

    fun setup(previewUrl: String?, onCompletion: () -> Unit) {
        if (previewUrl.isNullOrEmpty()) return

        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnCompletionListener {
                stop()
                onCompletion()
            }
        }
    }

    fun play(onPlay: () -> Unit) {
        mediaPlayer?.let {
            it.start()
            isPlaying = true
            onPlay()
        }
    }

    fun pause(onPause: () -> Unit) {
        mediaPlayer?.let {
            it.pause()
            isPlaying = false
            onPause()
        }
    }

    fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }

    fun isPlaying(): Boolean {
        return isPlaying
    }

    fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
}
