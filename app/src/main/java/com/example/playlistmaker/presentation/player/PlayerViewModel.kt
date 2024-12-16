package com.example.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.models.Track

class PlayerViewModel(private val playbackInteractor: IPlaybackInteractor) : ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> get() = _state

    private val _currentTime = MutableLiveData<String>()
    val currentTime: LiveData<String> get() = _currentTime

    private val handler = Handler(Looper.getMainLooper())
    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            if (playbackInteractor.isPlaying()) {
                _currentTime.postValue(playbackInteractor.getCurrentTimeFormatted())
                handler.postDelayed(this, 1000)
            }
        }
    }

    fun setupPlayer(track: Track) {
        playbackInteractor.setup(track.previewUrl) {
            _state.postValue(PlayerState.Prepared)
            stopTimer()
            resetTimer()
        }
    }

    fun play() {
        playbackInteractor.play {
            _state.postValue(PlayerState.Playing)
            startTimer()
        }
    }

    fun pause() {
        playbackInteractor.pause {
            _state.postValue(PlayerState.Paused)
            stopTimer()
        }
    }

    private fun startTimer() {
        handler.post(updateTimeRunnable)
    }

    private fun stopTimer() {
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun resetTimer() {
        _currentTime.postValue("00:00")
    }

    override fun onCleared() {
        super.onCleared()
        playbackInteractor.stop()
        stopTimer()
    }

    sealed class PlayerState {
        object Prepared : PlayerState()
        object Playing : PlayerState()
        object Paused : PlayerState()
        object Stopped : PlayerState()
    }
}
