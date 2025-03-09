package com.example.playlistmaker.presentation.media.playlist.playlistdetails

import androidx.recyclerview.widget.DiffUtil
import com.example.playlistmaker.domain.models.PlaylistTrack

class PlaylistTrackDiffCallback : DiffUtil.ItemCallback<PlaylistTrack>() {
    override fun areItemsTheSame(oldItem: PlaylistTrack, newItem: PlaylistTrack): Boolean {
        return oldItem.trackId == newItem.trackId
    }

    override fun areContentsTheSame(oldItem: PlaylistTrack, newItem: PlaylistTrack): Boolean {
        return oldItem == newItem
    }
}