package com.example.playlistmaker.presentation.media.favorite

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.models.Track

class FavoriteTrackDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
        return oldItem == newItem
    }
}