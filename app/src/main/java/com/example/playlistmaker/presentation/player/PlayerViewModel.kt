package com.example.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.models.Track

class PlayerViewModel(private val playbackInteractor: IPlaybackInteractor, private val handler:Handler) : ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> get() = _state

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            val currentTime = playbackInteractor.getCurrentTimeFormatted()
            _state.postValue(
                PlayerState.Active(
                    playbackState = PlayerState.PlaybackState.PLAYING,
                    currentTime = currentTime
                )
            )
            handler.postDelayed(this, 1000)
        }
    }

    init {
        _state.value = PlayerState.Active(
            playbackState = PlayerState.PlaybackState.PREPARED
        )
    }

    fun setupPlayer(track: Track) {
        playbackInteractor.setup(track.previewUrl) {
            _state.postValue(
                PlayerState.Active(
                    playbackState = PlayerState.PlaybackState.PREPARED
                )
            )
            stopTimer()
        }
    }

    fun play() {
        playbackInteractor.play {
            startTimer()
            _state.postValue(
                PlayerState.Active(
                    playbackState = PlayerState.PlaybackState.PLAYING
                )
            )
        }
    }

    fun pause() {
        playbackInteractor.pause {
            stopTimer()
            _state.postValue(
                PlayerState.Active(
                    playbackState = PlayerState.PlaybackState.PAUSED
                )
            )
        }
    }

    private fun startTimer() {
        handler.post(updateTimeRunnable)
    }

    private fun stopTimer() {
        handler.removeCallbacks(updateTimeRunnable)
    }

    fun stopPlayback() {
        playbackInteractor.stop()
        stopTimer()
        _state.postValue(
            PlayerState.Active(
                playbackState = PlayerState.PlaybackState.STOPPED
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopPlayback()
    }
}

