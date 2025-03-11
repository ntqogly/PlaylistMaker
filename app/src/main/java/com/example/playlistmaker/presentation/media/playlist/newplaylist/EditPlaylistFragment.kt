package com.example.playlistmaker.presentation.media.playlist.newplaylist

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.core.net.toUri
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {

    private val editViewModel: EditPlaylistViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setTitle(R.string.edit_playlist)

        binding.buttonCreateNewPlaylist.text = getString(R.string.save)

        val playlist = arguments?.getParcelable<Playlist>("playlist")
        playlist?.let {
            editViewModel.setPlaylistData(it)
        }

        editViewModel.playlistData.observe(viewLifecycleOwner) { playlist ->
            binding.playlistName.setText(playlist.name)
            binding.playlistDesc.setText(playlist.description)


            if (!playlist.coverPath.isNullOrEmpty()) {
                binding.imagePlace.setImageURI(playlist.coverPath.toUri())
            }
        }

        binding.buttonCreateNewPlaylist.setOnClickListener {
            editViewModel.updatePlaylist(
                binding.playlistName.text.toString(),
                binding.playlistDesc.text.toString()
            )
            findNavController().popBackStack()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
    }

}
