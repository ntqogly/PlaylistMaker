package com.example.playlistmaker.presentation.media.playlist.playlistdetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistTrack
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistDetailsViewModel>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var trackAdapter: PlaylistTrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val playlistId = arguments?.getLong("playlistId", -1) ?: -1
        if (playlistId == -1L) {
            return
        }

        setupBottomSheet()
        setupRecyclerView()

        viewModel.loadTracks(playlistId)

        lifecycleScope.launch {
            viewModel.getPlaylistDetails(playlistId).collect { playlist ->
                displayPlaylistDetails(playlist)
            }
        }

        lifecycleScope.launch {
            viewModel.tracks.collectLatest { trackList ->
                trackAdapter.updateTracks(trackList)
            }
        }


        lifecycleScope.launch {
            viewModel.getTotalDuration(playlistId).collect { totalDuration ->
                binding.playlistTotalDuration.text = totalDuration
            }
        }
        binding.detailsToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }


    }

    private fun setupBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_COLLAPSED
            isHideable = false
        }
    }

    private fun setupRecyclerView() {
        trackAdapter = PlaylistTrackAdapter(
            onTrackClick = { track -> openAudioPlayer(track) },
            onTrackLongClick = { track -> showDeleteTrackDialog(track) }
        )

        binding.rvPlaylistTracks.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = trackAdapter
        }
    }

    private fun openAudioPlayer(track: PlaylistTrack) {
        val bundle = Bundle().apply { putString("TRACK_EXTRA", Gson().toJson(track)) }
        findNavController().navigate(R.id.action_playlistDetailsFragment_to_fragmentPlayer, bundle)
    }

    private fun showDeleteTrackDialog(track: PlaylistTrack) {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Удалить трек")
            .setMessage("Хотите удалить трек?")
            .setPositiveButton("Да") { _, _ ->
                viewModel.deleteTrack(track.trackId.toLong(), arguments?.getLong("playlistId") ?: 0)
            }
            .setNegativeButton("Нет", null)
            .show()
    }

    private fun displayPlaylistDetails(playlist: Playlist) {
        binding.detailsPlaylistName.text = playlist.name
        binding.detailsDesc.text = playlist.description
        binding.detailsTracksCount.text =
            getString(R.string.track_count_placeholder, playlist.trackIds.size)

        if (!playlist.coverPath.isNullOrEmpty()) {
            Glide.with(binding.root.context).load(playlist.coverPath)
                .placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder)
                .into(binding.detailsImagePlace)
        } else {
            binding.detailsImagePlace.setImageResource(R.drawable.ic_place_holder)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
