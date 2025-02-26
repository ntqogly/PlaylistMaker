package com.example.playlistmaker.presentation.media.playlist.newplaylist

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<CreatePlaylistViewModel>()
    private var isDataChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            showExitConfirmationDialog()
        }


        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlistName.collectLatest {
                binding.etPlaylistName.editText                                  // ПЕРЕПРОВЕРИТЬ
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlistDescription.collectLatest {
                binding.etPlaylistDescription.editText                            // ПЕРЕПРОВЕРИТЬ
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.coverPath.collectLatest { coverPath ->
                coverPath?.let {
                    Glide.with(requireContext()).load(it).into(binding.imagePlace)
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isCreateButtonEnabled.collectLatest { isEnabled ->
                binding.buttonCreateNewPlaylist.isEnabled = isEnabled
            }
        }
    }

    private fun setupListeners() {
        binding.etPlaylistName.editText?.addTextChangedListener { text ->
            viewModel.updateName(text.toString())
        }

        binding.etPlaylistDescription.addOnEditTextAttachedListener {
            viewModel.updateDescription(it.toString())
        }

        binding.ivPlaylist.setOnClickListener {
            openGallery()
        }

        binding.buttonCreateNewPlaylist.setOnClickListener {
            viewModel.createPlaylist()
            Toast.makeText(
                requireContext(), "Плейлист ${binding.playlistName.text} создан", Toast.LENGTH_SHORT
            ).show()
            findNavController().navigateUp()
        }

        binding.toolbar.setOnClickListener {
            showExitConfirmationDialog()
//            findNavController().navigateUp()
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val imageUri = result.data!!.data!!
                val savedImagePath = saveImageToInternalStorage(imageUri)
                viewModel.updateCoverPath(savedImagePath)
                isDataChanged = true
            }
        }

    private fun saveImageToInternalStorage(imageUri: Uri): String {
        val context = requireContext()
        val inputStream: InputStream? = context.contentResolver.openInputStream(imageUri)
        val filename = "playlist_cover_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, filename)
        inputStream?.use { input ->
            FileOutputStream(file).use { output ->
                input.copyTo(output)
            }
        }
        return file.absolutePath
    }

    private fun showExitConfirmationDialog() {
        if (isDataChanged) {
            AlertDialog.Builder(requireContext()).setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setPositiveButton("Завершить") { _, _ ->
                    findNavController().navigateUp()
                }.setNegativeButton("Отмена", null).show()
        } else {
            findNavController().navigateUp()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val REQUEST_IMAGE_PICK = 1001
    }


}