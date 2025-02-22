package com.example.playlistmaker.presentation.player

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api.IFavoriteTrackInteractor
import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playbackInteractor: IPlaybackInteractor,
    private val favoriteTrackInteractor: IFavoriteTrackInteractor
) : ViewModel() {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> get() = _state

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private var progressJob: Job? = null
    private var currentTrack: Track? = null

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
            playbackState = PlayerState.PlaybackState.PREPARED, currentTime = "00:00"
        )
    }

    fun setupPlayer(track: Track) {
        track.previewUrl?.let {
            playbackInteractor.setup(it) {
                _state.postValue(
                    PlayerState.Active(
                        playbackState = PlayerState.PlaybackState.PREPARED, currentTime = "00:00"
                    )
                )
                stopTimer()
            }
        }
        viewModelScope.launch {
            val isTrackFavorite = favoriteTrackInteractor.isTrackFavorite(track.trackId.toString())
            _isFavorite.postValue(isTrackFavorite)
        }
    }

    fun play() {
        playbackInteractor.play {
            startTimer()
            val currentTime = playbackInteractor.getCurrentTimeFormatted()
            _state.postValue(
                PlayerState.Active(
                    playbackState = PlayerState.PlaybackState.PLAYING, currentTime = currentTime
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
                    playbackState = PlayerState.PlaybackState.PAUSED, currentTime = currentTime
                )
            )
        }
    }

    fun onFavoriteClicked(track: Track) {
        viewModelScope.launch {
            val currentState = _isFavorite.value ?: false
            if (currentState) {
                favoriteTrackInteractor.removeTrackFromFavorites(track)
            } else {
                favoriteTrackInteractor.addTrackToFavorites(track)
            }
            _isFavorite.postValue(!currentState)
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
                playbackState = PlayerState.PlaybackState.STOPPED, currentTime = "00:00"
            )
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopPlayback()
    }
}

