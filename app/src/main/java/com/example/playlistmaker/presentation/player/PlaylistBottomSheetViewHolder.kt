package com.example.playlistmaker.presentation.player

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackListItemBottomSheetBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistBottomSheetViewHolder(
    private val binding: TrackListItemBottomSheetBinding,
    private val onPlaylistClick: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        binding.bshPlaylistName.text = playlist.name
        binding.bshTracksCount.text = "${playlist.trackIds.size} треков"

        val coverPath = playlist.coverPath
        if (!coverPath.isNullOrEmpty()) {
            Glide.with(binding.root.context)
                .load(coverPath)
                .placeholder(R.drawable.ic_place_holder)
                .into(binding.bshPlaylistCover)
        } else {
            binding.bshPlaylistCover.setImageResource(R.drawable.ic_place_holder)
        }

        binding.root.setOnClickListener {
            onPlaylistClick(playlist.id)
        }
    }
}
