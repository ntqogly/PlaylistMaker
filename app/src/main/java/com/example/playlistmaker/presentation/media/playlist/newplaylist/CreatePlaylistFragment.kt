package com.example.playlistmaker.presentation.media.playlist.newplaylist

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.google.android.material.snackbar.Snackbar
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
    private var isImageChanged = false

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
        binding.etPlaylistName.requestFocus()
        showKeyboard(binding.etPlaylistName)


        binding.etPlaylistName.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) scrollToView(binding.etPlaylistName)
        }

        binding.etPlaylistDescription.editText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) scrollToView(binding.etPlaylistDescription)
        }
    }

    private fun scrollToView(view: View) {
        view.post {
            binding.scrollView.smoothScrollTo(0, view.bottom)
        }
    }


    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlistName.collectLatest {
                binding.etPlaylistName.editText
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.playlistDescription.collectLatest {
                binding.etPlaylistDescription.editText
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.coverPath.collectLatest { coverPath ->
                coverPath?.let {
                    val radiusPx = (8 * resources.displayMetrics.density)
                    Glide.with(requireContext()).load(coverPath).apply(
                        RequestOptions().transform(
                            CenterCrop(),
                            GranularRoundedCorners(radiusPx, radiusPx, radiusPx, radiusPx)
                        )
                    ).into(binding.imagePlace)
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
            showSnackBar(getString(R.string.playlist_created, binding.playlistName.text))
            findNavController().navigateUp()
        }

        binding.toolbar.setOnClickListener {
            showExitConfirmationDialog()
        }

    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePickerLauncher.launch(intent)
    }

    private fun showSnackBar(text: String) {
        val snack = Snackbar.make(requireView(), text, 3000)
        snack.view.setBackgroundResource(R.drawable.rounded_toast)
        val textView =
            snack.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        textView.textSize = 16f
        textView.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        textView.gravity = Gravity.CENTER
        snack.show()
    }

    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val imageUri = result.data!!.data!!
                val savedImagePath = saveImageToInternalStorage(imageUri)
                viewModel.updateCoverPath(savedImagePath)
                isImageChanged = true
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
        if (binding.playlistName.text?.isEmpty() == false || binding.playlistDesc.text?.isEmpty() == false || isImageChanged) {
            AlertDialog.Builder(requireContext()).setTitle(R.string.alert_dialog)
                .setMessage(R.string.alert_dialog_set_message)
                .setPositiveButton(R.string.alert_dialog_set_positive_button) { _, _ ->
                    findNavController().navigateUp()
                }.setNegativeButton(R.string.negative_button, null).show()
        } else {
            findNavController().navigateUp()
        }
    }

    private fun showKeyboard(view: View) {
        view.post {
            val inputMethodManager =
                requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}