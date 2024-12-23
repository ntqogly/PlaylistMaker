package com.example.playlistmaker.presentation.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra("TRACK_EXTRA")
        val track = Gson().fromJson(trackJson, Track::class.java)
        track?.let {
            displayTrackInfo(it)
            viewModel.setupPlayer(it)
        }

        binding.buttonPlayTrack.setOnClickListener {
            viewModel.state.value?.let { state ->
                when ((state as? PlayerState.Active)?.playbackState) {
                    PlayerState.PlaybackState.PLAYING -> viewModel.pause()
                    else -> viewModel.play()
                }
            }
        }

        binding.tbPlayer.setNavigationOnClickListener {
            finish()
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
            when (state) {
                is PlayerState.Active -> {
                    updatePlayButton(state.playbackState)
                    binding.currentTime.text = state.currentTime
                }
            }
        }
    }

    private fun updatePlayButton(playbackState: PlayerState.PlaybackState) {
        val icon = when (playbackState) {
            PlayerState.PlaybackState.PLAYING -> R.drawable.button_pause_track
            else -> R.drawable.button_play_track
        }
        binding.buttonPlayTrack.setImageResource(icon)
    }

    private fun displayTrackInfo(track: Track) {
        with(binding) {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            trackDuration.text = formatTime(track.trackTimeMillis)
            trackAlbum.text = track.trackAlbum
            trackReleaseDate.text = track.releaseDate?.substring(0, 4)
            trackGenre.text = track.genre
            trackCountry.text = track.trackCountry

            Glide.with(this@PlayerActivity)
                .load(track.artworkUrl100.replace("100x100", "600x600"))
                .into(trackCover)
        }
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}



