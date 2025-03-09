package com.example.playlistmaker.presentation.media.playlist.playlistdetails

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.playlistmaker.databinding.TrackListItemBinding
import com.example.playlistmaker.domain.models.PlaylistTrack

class PlaylistTrackAdapter(
    private val onTrackClick: (PlaylistTrack) -> Unit,
    private val onTrackLongClick: (PlaylistTrack) -> Unit
) : ListAdapter<PlaylistTrack, PlaylistTrackViewHolder>(PlaylistTrackDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistTrackViewHolder {
        val binding =
            TrackListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistTrackViewHolder(binding, onTrackClick, onTrackLongClick)
    }

    override fun onBindViewHolder(holder: PlaylistTrackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun updateTracks(newTracks: List<PlaylistTrack>) {
        submitList(newTracks)
    }
}
