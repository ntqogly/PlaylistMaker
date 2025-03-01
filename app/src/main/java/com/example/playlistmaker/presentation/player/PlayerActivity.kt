package com.example.playlistmaker.presentation.player

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.media.playlist.newplaylist.CreatePlaylistFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val viewModel by viewModel<PlayerViewModel>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var playlistAdapter: PlaylistBottomSheetAdapter

    private var isFavorite = false
    private lateinit var track: Track

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackJson = intent.getStringExtra("TRACK_EXTRA")
        val track = Gson().fromJson(trackJson, Track::class.java)
        track?.let {
//            displayTrackInfo(it)
            viewModel.setupPlayer(it)
            viewModel.loadPlaylists()
        }
        setupBottomSheet()
//        setupRecyclerView()

        binding.buttonPlayTrack.setOnClickListener {
            viewModel.state.value?.let { state ->
                when ((state as? PlayerState.Active)?.playbackState) {
                    PlayerState.PlaybackState.PLAYING -> viewModel.pause()
                    else -> viewModel.play()
                }
            }
        }

        binding.tbPlayer.setNavigationOnClickListener { finish() }
        binding.buttonLikeTrack.setOnClickListener { viewModel.onFavoriteClicked(track) }
        binding.buttonAddPlaylist.setOnClickListener { openPlaylistBottomSheet() }
//        observeViewModel()
        binding.newPlaylist.setOnClickListener { navigateToCreatePlaylist() }
    }



    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED, BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.overlay.visibility = View.VISIBLE
                            viewModel.loadPlaylists()
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }

                        else -> {}
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = slideOffset
                }
            })
        }
    }

//    private fun setupRecyclerView() {
//        playlistAdapter = PlaylistBottomSheetAdapter { playlistId ->
//            viewModel.addTrackToPlaylist(playlistId, track)
//            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        }
//
//        binding.bottomList.apply {
//            layoutManager = LinearLayoutManager(this@PlayerActivity)
//            adapter = playlistAdapter
//        }
//    }

    private fun openPlaylistBottomSheet() {
        viewModel.loadPlaylists()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun navigateToCreatePlaylist() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        val fragment = CreatePlaylistFragment()
        supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment)
            .addToBackStack(null) // Позволяет вернуться назад в PlayerActivity
            .commit()

    }


//    private fun observeViewModel() {
//        viewModel.state.observe(this) { state ->
//            when (state) {
//                is PlayerState.Active -> {
//                    updatePlayButton(state.playbackState)
//                    binding.currentTime.text = state.currentTime
//                }
//            }
//        }
//
//        viewModel.isFavorite.observe(this) { isFavorite ->
//            val icon =
//                if (isFavorite) R.drawable.button_liked_track else R.drawable.button_like_track
//            binding.buttonLikeTrack.setImageResource(icon)
//        }
//
//        lifecycleScope.launch {
//            viewModel.trackAdditionStatus.collectLatest { status ->
//                status?.let {
//                    Toast.makeText(this@PlayerActivity, it, Toast.LENGTH_SHORT).show()
//                    viewModel.clearTrackAdditionStatus()
//                }
//            }
//        }
//    }

    private fun updatePlayButton(playbackState: PlayerState.PlaybackState) {
        val icon = when (playbackState) {
            PlayerState.PlaybackState.PLAYING -> R.drawable.button_pause_track
            else -> R.drawable.button_play_track
        }
        binding.buttonPlayTrack.setImageResource(icon)
    }

//    private fun displayTrackInfo(track: Track) {
//        with(binding) {
//            tvTrackName.text = track.trackName
//            tvArtistName.text = track.artistName
//            trackDuration.text = formatTime(track.trackTimeMillis)
//            trackAlbum.text = track.trackAlbum
//            trackReleaseDate.text = track.releaseDate?.substring(0, 4)
//            trackGenre.text = track.genre
//            trackCountry.text = track.trackCountry
//
//
//            Glide.with(this@PlayerActivity).load(coverResolutionAmplifier(track.artworkUrl100))
//                .centerCrop().transform(RoundedCorners(10)).placeholder(R.drawable.ic_place_holder)
//                .into(trackCover)
//        }
//    }

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



