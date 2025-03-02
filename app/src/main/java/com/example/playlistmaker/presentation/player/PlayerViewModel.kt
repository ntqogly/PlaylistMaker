package com.example.playlistmaker.presentation.player

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.IFavoriteTrackInteractor
import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistTrack
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.PlaylistInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playbackInteractor: IPlaybackInteractor,
    private val favoriteTrackInteractor: IFavoriteTrackInteractor,
    private val playlistInteractor: PlaylistInteractor,
    private val application: Application
) : AndroidViewModel(application) {

    private val _state = MutableLiveData<PlayerState>()
    val state: LiveData<PlayerState> get() = _state

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    private val _playlists = MutableStateFlow<List<Playlist>>(emptyList())
    val playlists: StateFlow<List<Playlist>> = _playlists

    private val _isBottomSheetVisible = MutableStateFlow(false)
    val isBottomSheetVisible: StateFlow<Boolean> = _isBottomSheetVisible

    private val _trackAdditionStatus = MutableStateFlow<String?>(null)
    val trackAdditionStatus: StateFlow<String?> = _trackAdditionStatus

    private var progressJob: Job? = null

    fun loadPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists().collect { playlists ->
                _playlists.value = playlists
                playlists.forEach { playlist ->
                }

            }
        }
    }

    @SuppressLint("StringFormatInvalid")
    fun addTrackToPlaylist(playlistId: Long, track: Track) {
        viewModelScope.launch {
            val isTrackInDB =
                playlistInteractor.isTrackInPlaylistDB(track.trackId.toLong(), playlistId)
            val selectedPlaylist = _playlists.value.find { it.id == playlistId }
            val playlistName = selectedPlaylist?.name

            if (isTrackInDB) {
                _trackAdditionStatus.value = getApplication<Application>().getString(
                    R.string.track_already_added_to_playlist, playlistName
                )
            } else {
                val playlistTrack = PlaylistTrack(
                    trackId = track.trackId.toString(),
                    playlistId = playlistId,
                    trackName = track.trackName,
                    artistName = track.artistName,
                    trackAlbum = track.trackAlbum ?: "",
                    releaseDate = track.releaseDate ?: "",
                    genre = track.genre,
                    trackCountry = track.trackCountry,
                    trackTimeMillis = track.trackTimeMillis,
                    previewUrl = track.previewUrl,
                    artworkUrl100 = track.artworkUrl100
                )
                playlistInteractor.addTrackToPlaylist(playlistId, playlistTrack)
                _trackAdditionStatus.value = getApplication<Application>().getString(
                    R.string.track_added_to_playlist_toast, playlistName
                )
                delay(300)
                loadPlaylists()
            }
        }
    }


    fun showBottomSheet() {
        loadPlaylists()
        _isBottomSheetVisible.value = true
    }

    fun clearTrackAdditionStatus() {
        _trackAdditionStatus.value = null
    }

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

