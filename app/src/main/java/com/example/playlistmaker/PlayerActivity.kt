package com.example.playlistmaker

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
    private var mediaPlayer: MediaPlayer? = null
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var isPlaying = false

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
        binding.buttonPlayTrack.setOnClickListener { togglePlayback() }

        val track = IntentCompat.getParcelableExtra(intent, "track", Track::class.java)

        track?.let {
            val previewUrl = it.previewUrl
            if (previewUrl.isNotEmpty()) {
                setupMediaPlayer(previewUrl)
            }
            setupMediaPlayer(it.previewUrl)
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

        Glide.with(this).load(coverResolutionAmplifier()).centerCrop().transform(RoundedCorners(10))
            .placeholder(R.drawable.ic_place_holder).into(binding.trackCover)
    }
    private fun togglePlayback() {
        if (isPlaying) {
            pausePlayback()
        } else {
            startPlayback()
        }
    }

    private fun setupMediaPlayer(previewUrl: String?) {
        if (previewUrl.isNullOrEmpty()) return
        mediaPlayer = MediaPlayer().apply {
            setDataSource(previewUrl)
            prepareAsync()
            setOnPreparedListener {
                binding.buttonPlayTrack.isEnabled = true
            }
            setOnCompletionListener {
                pausePlayback()
            }
        }
    }

    private fun startPlayback() {
        mediaPlayer?.start()
        isPlaying = true
        binding.buttonPlayTrack.setImageResource(R.drawable.button_pause_track)
        updatePlaybackTime()
    }

    private fun pausePlayback() {
        mediaPlayer?.pause()
        isPlaying = false
        binding.buttonPlayTrack.setImageResource(R.drawable.button_play_track)
        handler.removeCallbacks(updateTimeRunnable)
    }

    private fun updatePlaybackTime() {
        handler.postDelayed(updateTimeRunnable, 1000)
    }

    private val updateTimeRunnable = object : Runnable {
        override fun run() {
            mediaPlayer?.let {
                val currentPosition = it.currentPosition
                binding.currentTime.text = millisToMinutesAndSeconds(currentPosition)
                if (isPlaying) handler.postDelayed(this, 1000)
            }
        }
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


//    private lateinit var binding: ActivityPlayerBinding
//    private var mediaPlayer: MediaPlayer? = null
//    private var handler: Handler = Handler(Looper.getMainLooper())
//    private var isPlaying = false
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivityPlayerBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val track = IntentCompat.getParcelableExtra(intent, "track", Track::class.java)
//
//        track?.let {
//            setupTrackInfo(it)
//            setupMediaPlayer(it.previewUrl)
//        }
//
//        binding.tbPlayer.setNavigationOnClickListener { finish() }
//        binding.buttonPlayTrack.setOnClickListener { togglePlayback() }
//    }
//
//    private fun setupTrackInfo(track: Track) {
//        with(binding) {
//            tvTrackName.text = track.trackName
//            tvArtistName.text = track.artistName
//            trackDuration.text = millisToMinutesAndSeconds(track.trackTimeMillis)
//            trackAlbum.text = track.trackAlbum
//            trackReleaseDate.text = track.releaseDate.substring(0, 4)
//            trackGenre.text = track.genre
//            trackCountry.text = track.trackCountry
//
//            Glide.with(this@PlayerActivity)
//                .load(track.artworkUrl100)
//                .transform(RoundedCorners(10))
//                .into(trackCover)
//        }
//    }
//
//    private fun setupMediaPlayer(previewUrl: String?) {
//        if (previewUrl.isNullOrEmpty()) return
//        mediaPlayer = MediaPlayer().apply {
//            setDataSource(previewUrl)
//            prepareAsync()
//            setOnPreparedListener {
//                binding.buttonPlayTrack.isEnabled = true
//            }
//            setOnCompletionListener {
//                stopPlayback()
//            }
//        }
//    }
//
//    private fun togglePlayback() {
//        if (isPlaying) {
//            pausePlayback()
//        } else {
//            startPlayback()
//        }
//    }
//
//    private fun startPlayback() {
//        mediaPlayer?.start()
//        isPlaying = true
//        binding.buttonPlayTrack.setImageResource(R.drawable.button_pause_track)
//        updatePlaybackTime()
//    }
//
//    private fun pausePlayback() {
//        mediaPlayer?.pause()
//        isPlaying = false
//        binding.buttonPlayTrack.setImageResource(R.drawable.button_play_track)
//        handler.removeCallbacks(updateTimeRunnable)
//    }
//
//    private fun stopPlayback() {
//        mediaPlayer?.seekTo(0)
//        isPlaying = false
//        binding.buttonPlayTrack.setImageResource(R.drawable.button_play_track)
//        binding.currentTime.text = getString(R.string._00_00)
//        handler.removeCallbacks(updateTimeRunnable)
//    }
//
//    private fun updatePlaybackTime() {
//        handler.postDelayed(updateTimeRunnable, 1000)
//    }
//
//    private val updateTimeRunnable = object : Runnable {
//        override fun run() {
//            mediaPlayer?.let {
//                val currentPosition = it.currentPosition
//                binding.currentTime.text = millisToMinutesAndSeconds(currentPosition)
//                if (isPlaying) handler.postDelayed(this, 1000)
//            }
//        }
//    }
//
//    override fun onStop() {
//        super.onStop()
//        pausePlayback()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer?.release()
//        mediaPlayer = null
//        handler.removeCallbacks(updateTimeRunnable)
//    }
//
//    private fun millisToMinutesAndSeconds(millis: Int): String {
//        val minutes = (millis / 1000) / 60
//        val seconds = (millis / 1000) % 60
//        return String.format("%02d:%02d", minutes, seconds)
//    }
