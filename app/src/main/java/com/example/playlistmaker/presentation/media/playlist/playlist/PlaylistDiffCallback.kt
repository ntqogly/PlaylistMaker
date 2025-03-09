package com.example.playlistmaker.presentation.media.playlist.playlist

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.models.Playlist

class PlaylistDiffCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }
}