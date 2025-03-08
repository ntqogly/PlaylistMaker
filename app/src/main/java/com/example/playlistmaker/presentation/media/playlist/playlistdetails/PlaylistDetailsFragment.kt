package com.example.playlistmaker.presentation.media.playlist.playlistdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistDetailsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
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

        lifecycleScope.launch {
            viewModel.getPlaylistDetails(playlistId).collect { playlist ->
                displayPlaylistDetails(playlist)
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

    private fun displayPlaylistDetails(playlist: Playlist) {
        binding.detailsPlaylistName.text = playlist.name
        binding.detailsDesc.text = playlist.description
        binding.detailsTracksCount.text = getString(R.string.track_count_placeholder, playlist.trackIds.size)

        if (!playlist.coverPath.isNullOrEmpty()) {
            Glide.with(binding.root.context)
                .load(playlist.coverPath)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_place_holder)
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
