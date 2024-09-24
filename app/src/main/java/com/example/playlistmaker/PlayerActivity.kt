package com.example.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.playlistmaker.models.Track

class PlayerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_player)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_player)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Получаем объект Track из Intent
        val track = intent.getParcelableExtra<Track>("track")

        // Проверяем, что объект track не null
        track?.let {
            findViewById<TextView>(R.id.tv_track_name).text = it.trackName
            findViewById<TextView>(R.id.tv_artist_name).text = it.artistName
            findViewById<TextView>(R.id.track_duration).text = millisToMinutesAndSeconds(it.trackTimeMillis)
            findViewById<TextView>(R.id.track_album).text = it.trackAlbum
            findViewById<TextView>(R.id.track_release_date).text = it.releaseDate.substring(0, 4)
            findViewById<TextView>(R.id.track_genre).text = it.genre
            findViewById<TextView>(R.id.track_country).text = it.trackCountry

            // Загрузка изображения обложки
            val coverImageView = findViewById<ImageView>(R.id.track_cover)
            Glide.with(this).load(it.artworkUrl100).into(coverImageView)
        }
    }

    private fun millisToMinutesAndSeconds(millis: Int): String {
        val minutes = (millis / 1000) / 60
        val seconds = (millis / 1000) % 60
        return String.format("%d:%02d", minutes, seconds)
    }
}
