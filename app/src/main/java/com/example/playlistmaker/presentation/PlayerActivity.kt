package com.example.playlistmaker.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.api.IPlaybackInteractor
import com.example.playlistmaker.domain.models.Track

//class PlayerActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityPlayerBinding
//    private val playbackInteractor: IPlaybackInteractor by lazy { Creator.providePlaybackInteractor() }
//    private val handler = Handler(Looper.getMainLooper())
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPlayerBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val track = intent.getParcelableExtra<Track>("TRACK_EXTRA")
//        track?.let {
//            displayTrackInfo(it)
//            playbackInteractor.setup(it.previewUrl) {
//                resetPlaybackState()
//            }
//        }
//
//        binding.buttonPlayTrack.setOnClickListener {
//            togglePlayback()
//        }
//
//        binding.tbPlayer.setNavigationOnClickListener {
//            onBackPressed()
//        }
//    }
//
//    override fun onBackPressed() {
//        playbackInteractor.stop()
//        super.onBackPressed()
//    }
//
//    private fun displayTrackInfo(track: Track) {
//        with(binding) {
//            tvTrackName.text = track.trackName
//            tvArtistName.text = track.artistName
//            trackDuration.text = formatTime(track.trackTimeMillis)
//            trackAlbum.text = track.trackAlbum.ifEmpty { "Unknown Album" }
//            trackReleaseDate.text = track.releaseDate.substring(0, 4)
//            trackGenre.text = track.genre.ifEmpty { "Unknown Genre" }
//            trackCountry.text = track.trackCountry.ifEmpty { "Unknown Country" }
//
//            Glide.with(this@PlayerActivity)
//                .load(track.artworkUrl100.replace("100x100", "600x600"))
//                .into(trackCover)
//        }
//    }
//
//    private fun togglePlayback() {
//        playbackInteractor.togglePlayback(
//            onPlay = {
//                updatePlayButton(isPlaying = true)
//                startTimer()
//            },
//            onPause = {
//                updatePlayButton(isPlaying = false)
//                stopTimer()
//            }
//        )
//    }
//
//    private fun startTimer() {
//        handler.post(object : Runnable {
//            override fun run() {
//                if (playbackInteractor.isPlaying()) {
//                    binding.currentTime.text = playbackInteractor.getCurrentTimeFormatted()
//                    handler.postDelayed(this, 1000)
//                }
//            }
//        })
//    }
//
//    private fun stopTimer() {
//        handler.removeCallbacksAndMessages(null)
//    }
//
//    private fun resetPlaybackState() {
//        updatePlayButton(isPlaying = false)
//        binding.currentTime.text = "00:00"
//        stopTimer()
//    }
//
//    private fun updatePlayButton(isPlaying: Boolean) {
//        binding.buttonPlayTrack.setImageResource(
//            if (isPlaying) R.drawable.button_pause_track else R.drawable.button_play_track
//        )
//    }
//
//    private fun formatTime(milliseconds: Int): String {
//        val minutes = milliseconds / 1000 / 60
//        val seconds = (milliseconds / 1000) % 60
//        return String.format("%02d:%02d", minutes, seconds)
//    }
//}


class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    private val viewModel: PlayerViewModel by viewModels {
        Creator.providePlayerViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getParcelableExtra<Track>("TRACK_EXTRA")
        track?.let {
            displayTrackInfo(it)
            viewModel.setupPlayer(it)
        }

        binding.buttonPlayTrack.setOnClickListener {
            viewModel.togglePlayback()
        }

        binding.tbPlayer.setNavigationOnClickListener {
            finish()
        }

        observeViewModel()
    }

    private fun displayTrackInfo(track: Track) {
        with(binding) {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            trackDuration.text = formatTime(track.trackTimeMillis)
            trackAlbum.text = track.trackAlbum.ifEmpty { "Unknown Album" }
            trackReleaseDate.text = track.releaseDate.substring(0, 4)
            trackGenre.text = track.genre.ifEmpty { "Unknown Genre" }
            trackCountry.text = track.trackCountry.ifEmpty { "Unknown Country" }

            Glide.with(this@PlayerActivity)
                .load(track.artworkUrl100.replace("100x100", "600x600"))
                .into(trackCover)
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is PlayerViewModel.PlayerState.Prepared -> resetPlaybackState()
                is PlayerViewModel.PlayerState.Playing -> updatePlayButton(isPlaying = true)
                is PlayerViewModel.PlayerState.Paused -> updatePlayButton(isPlaying = false)
                is PlayerViewModel.PlayerState.Stopped -> resetPlaybackState()
            }
        }

        viewModel.currentTime.observe(this) { currentTime ->
            binding.currentTime.text = currentTime
        }
    }

    private fun resetPlaybackState() {
        updatePlayButton(isPlaying = false)
        binding.currentTime.text = "00:00"
    }

    private fun updatePlayButton(isPlaying: Boolean) {
        binding.buttonPlayTrack.setImageResource(
            if (isPlaying) R.drawable.button_pause_track else R.drawable.button_play_track
        )
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

