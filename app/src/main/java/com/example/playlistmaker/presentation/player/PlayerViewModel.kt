package com.example.playlistmaker.presentation.player

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playbackInteractor: IPlaybackInteractor, private val handler: Handler
) : ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> get() = _state

    private var progressJob: Job? = null


    private suspend fun updateTimeRunnable() {
        while (playbackInteractor.isPlaying()) {
            val currentTime = playbackInteractor.getCurrentTimeFormatted()
            _state.postValue(
                PlayerState.Active(
                    playbackState = PlayerState.PlaybackState.PLAYING, currentTime = currentTime
                )
            )
            delay(300)
        }
    }

    init {
        _state.value = PlayerState.Active(
            playbackState = PlayerState.PlaybackState.PREPARED,
            currentTime = "00:00"
        )
    }

    fun setupPlayer(track: Track) {
        track.previewUrl?.let {
            playbackInteractor.setup(it) {
                _state.postValue(
                    PlayerState.Active(
                        playbackState = PlayerState.PlaybackState.PREPARED,
                        currentTime = "00:00"
                    )
                )
                stopTimer()
            }
        }
    }

    fun play() {
        playbackInteractor.play {
            startTimer()
            val currentTime = playbackInteractor.getCurrentTimeFormatted()
            _state.postValue(
                PlayerState.Active(
                    playbackState = PlayerState.PlaybackState.PLAYING,
                    currentTime = currentTime
                )
            )
        }
    }

    fun pause() {
        playbackInteractor.pause {
            stopTimer()
            val currentTime = playbackInteractor.getCurrentTimeFormatted()
            _state.postValue(
                PlayerState.Active(
                    playbackState = PlayerState.PlaybackState.PAUSED,
                    currentTime = currentTime
                )
            )
        }
    }

    private fun startTimer() {
        progressJob?.cancel()
        progressJob = viewModelScope.launch { updateTimeRunnable() }
    }

    private fun stopTimer() {
        progressJob?.cancel()
    }

    fun stopPlayback() {
        playbackInteractor.stop()
        stopTimer()
        _state.postValue(
            PlayerState.Active(
                playbackState = PlayerState.PlaybackState.STOPPED,
                currentTime = "00:00"
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopPlayback()
    }
}

