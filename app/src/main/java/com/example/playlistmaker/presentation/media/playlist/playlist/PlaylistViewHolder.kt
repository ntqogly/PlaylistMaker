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
        binding.tvPlaylistTrackCount.text = if (playlist.trackIds.isEmpty()) {
            "Треков нет"
        } else {
            formatTracksCount(playlist.trackIds.size)
        }

        if (!playlist.coverPath.isNullOrEmpty()) {
            Glide.with(binding.root.context).load(playlist.coverPath).into(binding.ivPlaylist)
        }

        binding.root.setOnClickListener {
            onPlaylistClick(playlist.id)
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
}
