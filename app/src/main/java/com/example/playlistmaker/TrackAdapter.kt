package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.models.Track

class TrackAdapter(
    private var tracks: MutableList<Track>,
    private val searchHistory: SearchHistory,
    private val onTrackClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackViewHolder>() {

    private fun moveTrackToTop(track: Track) {
        tracks.remove(track)
        tracks.add(0, track)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.track_list_item, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)

        holder.itemView.setOnClickListener {
            onTrackClick(track)
            moveTrackToTop(track)

            val context = holder.itemView.context
            val intent = Intent(context, PlayerActivity::class.java).apply {
                putExtra("track", track)
                putExtra("trackId", track.trackId)
                putExtra("trackName", track.trackName)
                putExtra("artistName", track.artistName)
                putExtra("trackTimeMillis", track.trackTimeMillis)
                putExtra("artworkUrl100", track.artworkUrl100)
                putExtra("trackCountry", track.trackCountry)
                putExtra("trackAlbum", track.trackAlbum)
                putExtra("genre", track.genre)
                putExtra("releaseDate", track.releaseDate)
            }
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return tracks.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateTracks(newTracks: List<Track>) {
        tracks = newTracks.toMutableList()
        notifyDataSetChanged()
    }
}