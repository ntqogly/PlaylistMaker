package com.example.playlistmaker.presentation.media

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.player.PlayerActivity
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<FavoriteViewModel>()

    private lateinit var favoriteTrackAdapter: FavoriteTrackAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val imageView = binding.ivNoResult
        setThemeSpecificImage(imageView)
        setupRecyclerViews()
        observeViewModel()
        viewModel.loadFavoriteTracks()
    }

    private fun setupRecyclerViews() {
        favoriteTrackAdapter = FavoriteTrackAdapter(mutableListOf()) { track ->
            openPlayerActivity(track)
        }
        binding.rvFavoriteTracks.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavoriteTracks.adapter = favoriteTrackAdapter
    }

    private fun observeViewModel() {
        viewModel.favoriteTracks.observe(viewLifecycleOwner) { tracks ->
            updateUI(tracks)
        }
    }

    private fun updateUI(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            showNoResult()
        } else {
            showFavoriteTracks(tracks)
        }
    }

    private fun showNoResult() {
        binding.rvFavoriteTracks.visibility = View.GONE
        binding.llNoResult.visibility = View.VISIBLE
    }

    private fun showFavoriteTracks(tracks: List<Track>) {
        binding.rvFavoriteTracks.visibility = View.VISIBLE
        binding.llNoResult.visibility = View.GONE
        favoriteTrackAdapter.updateTracks(tracks)
    }

    private fun openPlayerActivity(track: Track) {
//        val bundle = Bundle().apply {
//            putString("TRACK_EXTRA", Gson().toJson(track))
//        }
//        findNavController().navigate(R.id.playerFragment, bundle)
        val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra("TRACK_EXTRA", Gson().toJson(track))
        }
        startActivity(intent)
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
        fun newInstance(): FavoriteFragment {
            return FavoriteFragment()
        }
    }
}