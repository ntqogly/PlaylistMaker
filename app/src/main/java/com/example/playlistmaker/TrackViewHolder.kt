package com.example.playlistmaker

import android.view.RoundedCorner
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.TrackListItemBinding
import org.w3c.dom.Text

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = TrackListItemBinding.bind(itemView)

    fun bind(model: Track) {
        with(binding) {
            trackName.text = model.trackTime
            artistName.text = model.artistName
            trackTime.text = model.trackTime
            Glide.with(itemView).load(model.artworkUrl100).centerCrop().transform(RoundedCorners(2))
                .into(trackImage)
        }
    }
}