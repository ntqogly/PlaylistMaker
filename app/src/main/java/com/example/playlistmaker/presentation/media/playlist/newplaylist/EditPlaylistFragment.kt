package com.example.playlistmaker.presentation.media.playlist.newplaylist

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Playlist
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditPlaylistFragment : CreatePlaylistFragment() {

    private val editViewModel: EditPlaylistViewModel by viewModel()
    private var selectedCoverPath: String? = null

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val imageUri: Uri? = result.data?.data
            imageUri?.let {
                selectedCoverPath = it.toString()
                binding.imagePlace.setImageURI(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setTitle(getString(R.string.edit_playlist))

        binding.buttonCreateNewPlaylist.text = getString(R.string.save)

        val playlist = arguments?.getParcelable<Playlist>("playlist")
        playlist?.let {
            editViewModel.setPlaylistData(it)
            selectedCoverPath = it.coverPath
        }

        editViewModel.playlistData.observe(viewLifecycleOwner) { playlist ->
            binding.playlistName.setText(playlist.name)
            binding.playlistDesc.setText(playlist.description)

            if (!playlist.coverPath.isNullOrEmpty()) {
                binding.imagePlace.setImageURI(Uri.parse(playlist.coverPath))
            }
        }

        binding.ivPlaylist.setOnClickListener {
            pickImage()
        }

        binding.buttonCreateNewPlaylist.setOnClickListener {
            editViewModel.updatePlaylist(
                binding.playlistName.text.toString(),
                binding.playlistDesc.text.toString(),
                selectedCoverPath
            )
            findNavController().popBackStack()
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePickerLauncher.launch(intent)
    }
}
