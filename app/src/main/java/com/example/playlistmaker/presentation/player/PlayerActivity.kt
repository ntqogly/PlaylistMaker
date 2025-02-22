package com.example.playlistmaker.presentation.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.api.IFavoriteTrackInteractor
import com.example.playlistmaker.domain.models.Track
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

    private val favoriteTrackInteractor: IFavoriteTrackInteractor by inject()
    private var isFavorite = false
    private lateinit var track: Track

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
        binding.buttonLikeTrack.setOnClickListener {
            viewModel.onFavoriteClicked(track)
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
        viewModel.isFavorite.observe(this) { isFavorite ->
            val icon =
                if (isFavorite) R.drawable.button_liked_track else R.drawable.button_like_track
            binding.buttonLikeTrack.setImageResource(icon)
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


            Glide.with(this@PlayerActivity).load(coverResolutionAmplifier(track.artworkUrl100))
                .centerCrop().transform(RoundedCorners(10)).placeholder(R.drawable.ic_place_holder)
                .into(trackCover)
        }
    }

    private fun updateFavoriteButton() {
        val iconRes = if (isFavorite) {
            R.drawable.button_liked_track
        } else {
            R.drawable.button_like_track
        }
        binding.buttonLikeTrack.setImageResource(iconRes)
    }

    private fun coverResolutionAmplifier(artworkUrl100: String?): String? {
        return artworkUrl100?.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onPause() {
        super.onPause()
        if (viewModel.state.value is PlayerState.Active && (viewModel.state.value as PlayerState.Active).playbackState == PlayerState.PlaybackState.PLAYING) {
            viewModel.pause()
        }
    }
}



