package com.example.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import org.w3c.dom.Text

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val name: TextView = itemView.findViewById(R.id.track_name)
    private val performer: TextView = itemView.findViewById(R.id.artist_name)
    private val time: TextView = itemView.findViewById(R.id.track_time)
    private val image: ImageView = itemView.findViewById(R.id.track_image)

    fun bind(model: Track) {
        name.text = model.trackTime
        performer.text = model.artistName
        time.text = model.trackTime
        Glide.with(itemView).load(model.artworkUrl100).centerCrop().transform(RoundedCorners(2))
            .into(image)
    }
}