package com.example.playlistmaker.presentation.media.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FavoriteTrackAdapter(
    private var tracks: MutableList<Track>, private val onTrackClick: (Track) -> Unit
) : ListAdapter<Track, FavoriteTrackViewHolder>(FavoriteTrackDiffCallback()) {

    private var clickJob: Job? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
        return FavoriteTrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteTrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener {
            clickJob?.cancel()
            clickJob = CoroutineScope(Dispatchers.Main).launch {
                delay(500)
                onTrackClick(track)
            }
        }
    }

    override fun getItemCount(): Int = tracks.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newTracks: List<Track>) {
        tracks.clear()
        tracks.addAll(newTracks)
        notifyDataSetChanged()
    }
}