package com.example.playlistmaker.presentation

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

    fun setupPlayer(track: Track) {
        playbackInteractor.setup(track.previewUrl) {
            _state.postValue(PlayerState.Prepared)
        }
    }

    fun play() {
        playbackInteractor.play {
            _state.postValue(PlayerState.Playing)
        }
    }

    fun pause() {
        playbackInteractor.pause {
            _state.postValue(PlayerState.Paused)
        }
    }

    fun stop() {
        playbackInteractor.stop()
        _state.postValue(PlayerState.Stopped)
    }

    fun togglePlayback() {
        playbackInteractor.togglePlayback(
            onPlay = { _state.postValue(PlayerState.Playing) },
            onPause = { _state.postValue(PlayerState.Paused) }
        )
    }

    fun updateCurrentTime() {
        _currentTime.postValue(playbackInteractor.getCurrentTimeFormatted())
    }

    sealed class PlayerState {
        object Prepared : PlayerState()
        object Playing : PlayerState()
        object Paused : PlayerState()
        object Stopped : PlayerState()
    }
}
