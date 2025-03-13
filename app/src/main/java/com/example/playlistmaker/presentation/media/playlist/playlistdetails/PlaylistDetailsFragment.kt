package com.example.playlistmaker.presentation.media.playlist.playlistdetails

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.domain.models.Playlist
import com.example.playlistmaker.domain.models.PlaylistTrack
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistDetailsFragment : Fragment() {

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<PlaylistDetailsViewModel>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>
    private lateinit var menuBottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
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

        observeViewModel(playlistId)

        onClickListeners(playlistId)


    }

    private fun observeViewModel(playlistId: Long) {
        lifecycleScope.launch {
            viewModel.getPlaylistDetails(playlistId).collect { playlist ->
                displayPlaylistDetails(playlist)
            }
        }

        lifecycleScope.launch {
            viewModel.tracks.collectLatest { trackList ->
                trackAdapter.updateTracks(trackList)
                if (trackList.isEmpty()) {
                    val snackbar = Snackbar.make(binding.root, "Треков нет", Snackbar.LENGTH_SHORT)

                    // 🔹 Получаем TextView внутри Snackbar
                    val snackbarView = snackbar.view
                    val textView =
                        snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)

                    // 🔹 Выравниваем текст по центру
                    textView.gravity = Gravity.CENTER
                    textView.textAlignment = View.TEXT_ALIGNMENT_CENTER

                    snackbar.show()
                }
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

        menuBottomSheetBehavior = BottomSheetBehavior.from(binding.bshMenu).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
            isHideable = true
        }

    }

    private fun setupRecyclerView() {
        trackAdapter = PlaylistTrackAdapter(onTrackClick = { track -> openAudioPlayer(track) },
            onTrackLongClick = { track -> showDeleteTrackDialog(track) })

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
        androidx.appcompat.app.AlertDialog.Builder(requireContext()).setTitle("")
            .setMessage("Хотите удалить трек?").setPositiveButton("Да") { _, _ ->
                viewModel.deleteTrack(track.trackId.toLong(), arguments?.getLong("playlistId") ?: 0)
            }.setNegativeButton("Нет", null).show()
    }

    private fun displayPlaylistDetails(playlist: Playlist) {
        binding.detailsPlaylistName.text = playlist.name
        binding.detailsDesc.text = playlist.description
        binding.detailsTracksCount.text = if (playlist.trackIds.isEmpty()) {
            "Треков нет"
        } else {
            formatTracksCount(playlist.trackIds.size)
        }

        if (!playlist.coverPath.isNullOrEmpty()) {
            Glide.with(binding.root.context).load(playlist.coverPath)
                .placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder)
                .into(binding.detailsImagePlace)
        } else {
            binding.detailsImagePlace.setImageResource(R.drawable.ic_place_holder)
        }
    }

    private fun formatTracksCount(count: Int): String {
        val lastDigit = count % 10
        val lastTwoDigits = count % 100

        return when {
            lastTwoDigits in 11..19 -> "$count треков"
            lastDigit == 1 -> "$count трек"
            lastDigit in 2..4 -> "$count трека"
            else -> "$count треков"
        }
    }

    private fun onClickListeners(playlistId: Long) {
        binding.detailsToolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.detailsShare.setOnClickListener {
            sharePlaylist(playlistId)
        }

        binding.detailsThreeDot.setOnClickListener {
            attachDataToMenu()
            binding.bshMenu.visibility = View.VISIBLE
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }

        binding.bshMenuPlaylistShare.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            sharePlaylist(playlistId)
        }

        binding.bshMenuPlaylistDelete.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            showDeletePlaylistDialog(playlistId)
        }

        binding.bshMenuPlaylistEdit.setOnClickListener {
            menuBottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            navigateToEditPlaylist(playlistId)
        }
    }

    private fun attachDataToMenu() {
        val playlistId = arguments?.getLong("playlistId") ?: return

        lifecycleScope.launch {
            viewModel.getPlaylistDetails(playlistId).collectLatest { playlist ->
                binding.bshMenuPlaylistName.text = playlist.name
                binding.bshMenuPlaylistTracksCount.text = if (playlist.trackIds.isEmpty()) {
                    "Треков нет"
                } else {
                    formatTracksCount(playlist.trackIds.size)
                }

                Glide.with(requireContext()).load(playlist.coverPath ?: R.drawable.ic_place_holder)
                    .transform(
                        RoundedCorners(10)
                    ).placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder)
                    .into(binding.bshMenuImagePlace)
            }
        }
    }

    private fun sharePlaylist(playlistId: Long) {
        lifecycleScope.launch {
            viewModel.getPlaylistDetails(playlistId).collectLatest { playlist ->
                val tracks = viewModel.tracks.value
                if (tracks.isEmpty()) {
                    Toast.makeText(
                        requireContext(),
                        "В этом плейлисте нет списка треков, которым можно поделиться",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@collectLatest
                }

                val shareText = buildShareText(playlist, tracks)
                val intent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }
                startActivity(Intent.createChooser(intent, "Поделиться плейлистом"))
            }
        }
    }

    private fun buildShareText(playlist: Playlist, tracks: List<PlaylistTrack>): String {
        val trackListText = tracks.mapIndexed { index, track ->
            "${index + 1}. ${track.artistName} - ${track.trackName} (${formatDuration(track.trackTimeMillis)})"
        }.joinToString("\n")

        return """
            ${playlist.name}
            ${playlist.description}
            ${formatTracksCount(tracks.size)}
            
            $trackListText
        """.trimIndent()
    }

    private fun formatDuration(durationMillis: Int): String {
        val minutes = (durationMillis / 60000)
        val seconds = ((durationMillis % 60000) / 1000)
        return "$minutes:${seconds.toString().padStart(2, '0')}"
    }

    private fun showDeletePlaylistDialog(playlistId: Long) {
        lifecycleScope.launch {
            viewModel.getPlaylistDetails(playlistId).collectLatest { playlist ->
                val dialog = AlertDialog.Builder(requireContext())
                    .setMessage("Хотите удалить плейлист «${playlist.name}»?")
                    .setPositiveButton("Да") { _, _ ->
                        lifecycleScope.launch {
                            viewModel.deletePlaylist(playlistId) {
                                findNavController().popBackStack()
                            }
                        }
                    }.setNegativeButton("Нет", null).create()

                dialog.setOnShowListener {
                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        requireContext().getColor(R.color.pb_blue)
                    )
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        requireContext().getColor(R.color.pb_blue)
                    )
                }

                dialog.show()
            }
        }
    }

    private fun navigateToEditPlaylist(playlistId: Long) {
        lifecycleScope.launch {
            viewModel.getPlaylistDetails(playlistId).collectLatest { playlist ->
                val bundle = Bundle().apply {
                    putParcelable("playlist", playlist)
                }
                findNavController().navigate(
                    R.id.action_playlistDetailsFragment_to_editPlaylistFragment, bundle
                )
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        lifecycleScope.coroutineContext.cancelChildren()
    }
}
