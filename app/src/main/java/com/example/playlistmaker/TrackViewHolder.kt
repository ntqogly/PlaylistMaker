package com.example.playlistmaker

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.TrackListItemBinding

class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding = TrackListItemBinding.bind(itemView)

    fun bind(model: Track) {
        with(binding) {
            trackName.text = model.trackName
            artistName.text = model.artistName
            trackTime.text = model.trackTime
            Glide.with(itemView).load(model.artworkUrl100).centerCrop()
                .transform(RoundedCorners(10))
                .placeholder(R.drawable.ic_place_holder).into(trackImage)
        }
    }
}