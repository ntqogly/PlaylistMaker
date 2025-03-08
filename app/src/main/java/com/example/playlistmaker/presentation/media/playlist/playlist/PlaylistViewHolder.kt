package com.example.playlistmaker.presentation.media.playlist.playlist

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.databinding.PlaylistItemBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistViewHolder(
    private val binding: PlaylistItemBinding, private val onPlaylistClick: (Long) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(playlist: Playlist) {
        binding.tvPlaylistName.text = playlist.name
        binding.tvPlaylistTrackCount.text = "${playlist.trackIds.size} треков"

        if (!playlist.coverPath.isNullOrEmpty()) {
            Glide.with(binding.root.context).load(playlist.coverPath).into(binding.ivPlaylist)
        }

        binding.root.setOnClickListener {
            onPlaylistClick(playlist.id)
        }
    }
}
