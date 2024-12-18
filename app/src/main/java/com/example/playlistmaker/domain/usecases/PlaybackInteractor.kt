package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.domain.api.IPlaybackInteractor

class PlaybackInteractor(private val iPlaybackInteractor: IPlaybackInteractor) :
    IPlaybackInteractor {

    override fun setup(url: String, onComplete: () -> Unit) {
        iPlaybackInteractor.setup(url, onComplete)
    }

    override fun play(onComplete: () -> Unit) {
        iPlaybackInteractor.play(onComplete)
    }

    override fun pause(onComplete: () -> Unit) {
        iPlaybackInteractor.pause(onComplete)
    }

    override fun stop() {
        iPlaybackInteractor.stop()
    }

    override fun isPlaying(): Boolean {
        return iPlaybackInteractor.isPlaying()
    }

    override fun getCurrentPosition(): Long {
        return iPlaybackInteractor.getCurrentPosition()
    }

    override fun togglePlayback(onPlay: () -> Unit, onPause: () -> Unit) {
        if (iPlaybackInteractor.isPlaying()) {
            iPlaybackInteractor.pause(onPause)
        } else {
            iPlaybackInteractor.play(onPlay)
        }
    }

    override fun release() {
        iPlaybackInteractor.release()
    }

    override fun getCurrentTimeFormatted(): String {
        return iPlaybackInteractor.getCurrentTimeFormatted()
    }
}
