package com.example.playlistmaker

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.IntentCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.models.Track

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.tbPlayer.setNavigationOnClickListener { finish() }

        val track = IntentCompat.getParcelableExtra(intent, "track", Track::class.java)

        track?.let {
            with(binding) {
                tvTrackName.text = it.trackName
                tvArtistName.text = it.artistName
                trackDuration.text = millisToMinutesAndSeconds(it.trackTimeMillis)
                trackAlbum.text = it.trackAlbum
                trackReleaseDate.text = it.releaseDate.substring(0, 4)
                trackGenre.text = it.genre
                trackCountry.text = it.trackCountry
            }
            Glide.with(this).load(it.artworkUrl100).into(binding.trackCover)
        }

        Glide.with(this)
            .load(coverResolutionAmplifier())
            .centerCrop()
            .transform(RoundedCorners(10))
            .placeholder(R.drawable.ic_place_holder).into(binding.trackCover)
    }

    private fun coverResolutionAmplifier(): String? {
        return intent.getStringExtra("artworkUrl100")?.replaceAfterLast('/', "512x512bb.jpg")
    }

    private fun millisToMinutesAndSeconds(millis: Int): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }
}
