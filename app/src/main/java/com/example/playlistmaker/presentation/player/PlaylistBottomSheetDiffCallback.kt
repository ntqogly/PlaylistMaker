package com.example.playlistmaker.presentation.player

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.models.Playlist

class PlaylistBottomSheetDiffCallback : DiffUtil.ItemCallback<Playlist>() {
    override fun areItemsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Playlist, newItem: Playlist): Boolean {
        return oldItem == newItem
    }
}