package com.example.playlistmaker.presentation.media.playlist.playlist

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.domain.models.Playlist
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment : Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistViewModel>()
    private lateinit var playlistAdapter: PlaylistAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = binding.ivNoResult
        setThemeSpecificImage(imageView)

        setupRecyclerView()
        observeViewModel()

        binding.buttonCreatePlaylist.setOnClickListener {
            findNavController().navigate(R.id.action_fragmentMedia_to_fragmentCreatePlaylist)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadPlaylists()
    }

    private fun setupRecyclerView() {
        playlistAdapter = PlaylistAdapter(emptyList())
        val itemSpacing = resources.getDimensionPixelSize(R.dimen.platlist_item_space)
        binding.rvPlaylist.addItemDecoration(GridSpacingItemDecoration(2, itemSpacing))
        binding.rvPlaylist.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlaylist.adapter = playlistAdapter
    }

    private fun observeViewModel() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlists.collectLatest { playlists ->
                updateUI(playlists)
            }
        }
    }

    private fun updateUI(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            binding.rvPlaylist.visibility = View.GONE
            binding.ivNoResult.visibility = View.VISIBLE
            binding.tvNoResult.visibility = View.VISIBLE //
        } else {
            binding.rvPlaylist.visibility = View.VISIBLE
            binding.ivNoResult.visibility = View.GONE
            binding.tvNoResult.visibility = View.GONE //
            playlistAdapter.updateData(playlists)
        }
    }


    private fun setThemeSpecificImage(imageView: ImageView) {
        val isNightMode =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        if (isNightMode) {
            imageView.setImageResource(R.drawable.img_search_dark)
        } else {
            imageView.setImageResource(R.drawable.img_search_light)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        fun newInstance(): PlaylistFragment {
            return PlaylistFragment()
        }
    }
}
