package com.example.playlistmaker.domain.api

interface IPlaybackInteractor {
    fun play(onComplete: () -> Unit)
    fun pause(onComplete: () -> Unit)
    fun stop()
    fun isPlaying(): Boolean
    fun setup(url: String, onComplete: () -> Unit)
    fun getCurrentPosition(): Long
    fun togglePlayback(onPlay: () -> Unit, onPause: () -> Unit)
    fun getCurrentTimeFormatted(): String
    fun release()

}