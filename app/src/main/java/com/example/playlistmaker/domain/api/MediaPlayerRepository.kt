package com.example.playlistmaker.domain.api

interface MediaPlayerRepository {
    fun setup(url: String, onCompletion: () -> Unit)
    fun play(onPlay: () -> Unit)
    fun pause(onPause: () -> Unit)
    fun stop()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Long
}
