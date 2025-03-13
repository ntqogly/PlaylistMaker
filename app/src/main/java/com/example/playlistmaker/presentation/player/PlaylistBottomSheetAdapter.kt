package com.example.playlistmaker.presentation.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.databinding.TrackListItemBottomSheetBinding
import com.example.playlistmaker.domain.models.Playlist

class PlaylistBottomSheetAdapter(
    private val onPlaylistClick: (Long) -> Unit
) : ListAdapter<Playlist, PlaylistBottomSheetViewHolder>(PlaylistBottomSheetDiffCallback()) {

    private var playlists: List<Playlist> = emptyList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaylistBottomSheetViewHolder {
        val binding = TrackListItemBottomSheetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return PlaylistBottomSheetViewHolder(binding, onPlaylistClick)
    }

    override fun onBindViewHolder(holder: PlaylistBottomSheetViewHolder, position: Int) {
        holder.bind(playlists[position])
    }

    override fun getItemCount(): Int = playlists.size

    fun updateData(newPlaylists: List<Playlist>) {
        playlists = newPlaylists
        notifyDataSetChanged()
    }
}
