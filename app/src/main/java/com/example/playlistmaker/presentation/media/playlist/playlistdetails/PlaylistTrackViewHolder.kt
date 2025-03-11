package com.example.playlistmaker.presentation.media.playlist.playlistdetails

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackListItemBinding
import com.example.playlistmaker.domain.models.PlaylistTrack
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistTrackViewHolder(
    private val binding: TrackListItemBinding,
    private val onTrackClick: (PlaylistTrack) -> Unit,
    private val onTrackLongClick: (PlaylistTrack) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(track: PlaylistTrack) {
        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = formatTrackDuration(track.trackTimeMillis)


        Glide.with(binding.root.context).load(track.artworkUrl100)
            .placeholder(R.drawable.ic_place_holder).error(R.drawable.ic_place_holder).transform(
                RoundedCorners(10)
            ).into(binding.trackImage)

        binding.root.setOnClickListener { onTrackClick(track) }

        binding.root.setOnLongClickListener {
            onTrackLongClick(track)
            true
        }
    }

    private fun formatTrackDuration(durationMillis: Int): String {
        val dateFormat = SimpleDateFormat("mm:ss", Locale.getDefault())
        return dateFormat.format(durationMillis)
    }
}
