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
            playbackState = PlayerState.PlaybackState.PREPARED
        )
    }

    fun setupPlayer(track: Track) {
        track.previewUrl?.let {
            playbackInteractor.setup(it) {
                _state.postValue(
                    PlayerState.Active(
                        playbackState = PlayerState.PlaybackState.PREPARED
                    )
                )
                stopTimer()
            }
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
                playbackState = PlayerState.PlaybackState.STOPPED
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopPlayback()
    }
}

