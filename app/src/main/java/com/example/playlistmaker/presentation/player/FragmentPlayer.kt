package com.example.playlistmaker.presentation.player

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentPlayer : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModel<PlayerViewModel>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var playlistAdapter: PlaylistBottomSheetAdapter

    private var isFavorite = false
    private var track: Track? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackJson = arguments?.getString("TRACK_EXTRA")
        if (trackJson != null) {
            track = Gson().fromJson(trackJson, Track::class.java)
        } else {
        }
        val track = Gson().fromJson(trackJson, Track::class.java)
        track?.let {
            displayTrackInfo(it)
            viewModel.setupPlayer(it)
            viewModel.loadPlaylists()
        }
        setupBottomSheet()
        setupRecyclerView()
        observeViewModel()

        binding.buttonPlayTrack.setOnClickListener {
            viewModel.state.value?.let { state ->
                when ((state as? PlayerState.Active)?.playbackState) {
                    PlayerState.PlaybackState.PLAYING -> viewModel.pause()
                    else -> viewModel.play()
                }
            }
        }

        binding.tbPlayer.setNavigationOnClickListener { requireActivity().onBackPressedDispatcher.onBackPressed() }
        binding.buttonLikeTrack.setOnClickListener { viewModel.onFavoriteClicked(track) }
        binding.buttonAddPlaylist.setOnClickListener { openPlaylistBottomSheet() }
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
                            binding.overlay.alpha = 0.6f // ✅ Затемнение при открытии
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.overlay.visibility = View.GONE
                        }
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    binding.overlay.alpha = slideOffset // ✅ Плавное затемнение при движении
                }
            })
        }
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistBottomSheetAdapter { playlistId ->
            if (track != null) {
                viewModel.addTrackToPlaylist(playlistId, track!!)
            }
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.bottomList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = playlistAdapter
        }

        lifecycleScope.launch {
            viewModel.playlists.collectLatest { playlists ->
                playlistAdapter.updateData(playlists)
            }
        }
    }


    private fun openPlaylistBottomSheet() {
        viewModel.showBottomSheet()
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun navigateToCreatePlaylist() {
        findNavController().navigate(R.id.action_fragmentPlayer_to_fragmentCreatePlaylist)
    }

    private fun observeViewModel() {
        viewModel.state.observe(requireActivity()) { state ->
            when (state) {
                is PlayerState.Active -> {
                    updatePlayButton(state.playbackState)
                    binding.currentTime.text = state.currentTime
                }
            }
        }

        viewModel.isFavorite.observe(requireActivity()) { isFavorite ->
            val icon =
                if (isFavorite) R.drawable.button_liked_track else R.drawable.button_like_track
            binding.buttonLikeTrack.setImageResource(icon)
        }

        lifecycleScope.launch {
            viewModel.trackAdditionStatus.collectLatest { status ->
                status?.let {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    viewModel.clearTrackAdditionStatus()
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


            Glide.with(requireContext()).load(coverResolutionAmplifier(track.artworkUrl100))
                .centerCrop().transform(RoundedCorners(10)).placeholder(R.drawable.ic_place_holder)
                .into(trackCover)
        }
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



