package com.example.playlistmaker.presentation

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.PlaybackInteractor

//class PlayerActivity : AppCompatActivity() {
//
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
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_player)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//
//        binding.tbPlayer.setNavigationOnClickListener { finish() }
//        binding.buttonPlayTrack.setOnClickListener { togglePlayback() }
//
//        val track = IntentCompat.getParcelableExtra(intent, "track", Track::class.java)
//
//        track?.let {
//            val previewUrl = it.previewUrl
//            if (previewUrl.isNotEmpty()) {
//                setupMediaPlayer(previewUrl)
//            }
//            setupMediaPlayer(it.previewUrl)
//            with(binding) {
//                tvTrackName.text = it.trackName
//                tvArtistName.text = it.artistName
//                trackDuration.text = millisToMinutesAndSeconds(it.trackTimeMillis)
//                trackAlbum.text = it.trackAlbum
//                trackReleaseDate.text = it.releaseDate.substring(0, 4)
//                trackGenre.text = it.genre
//                trackCountry.text = it.trackCountry
//            }
//            Glide.with(this).load(it.artworkUrl100).into(binding.trackCover)
//        }
//
//        Glide.with(this).load(coverResolutionAmplifier()).centerCrop().transform(RoundedCorners(10))
//            .placeholder(R.drawable.ic_place_holder).into(binding.trackCover)
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
//    private fun setupMediaPlayer(previewUrl: String?) {
//        if (previewUrl.isNullOrEmpty()) return
//        mediaPlayer = MediaPlayer().apply {
//            setDataSource(previewUrl)
//            prepareAsync()
//            setOnPreparedListener {
//                binding.buttonPlayTrack.isEnabled = true
//            }
//            setOnCompletionListener {
//                pausePlayback()
//                binding.currentTime.text = millisToMinutesAndSeconds(0)
//            }
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
//    private fun updatePlaybackTime() {
//        handler.postDelayed(updateTimeRunnable, 1000)
//    }
//
//    override fun onBackPressed() {
//        releaseMediaPlayer()
//        super.onBackPressed()
//    }
//    override fun onDestroy() {
//        super.onDestroy()
//        releaseMediaPlayer()
//    }
//    private fun releaseMediaPlayer() {
//        mediaPlayer?.let {
//            if (it.isPlaying) it.stop()
//            it.release()
//        }
//        mediaPlayer = null
//        handler.removeCallbacks(updateTimeRunnable)
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
//    private fun coverResolutionAmplifier(): String? {
//        return intent.getStringExtra("artworkUrl100")?.replaceAfterLast('/', "512x512bb.jpg")
//    }
//
//    private fun millisToMinutesAndSeconds(millis: Int): String {
//        val minutes = (millis / 1000) / 60
//        val seconds = (millis / 1000) % 60
//        return String.format("%d:%02d", minutes, seconds)
//    }
//}


//class PlayerActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivityPlayerBinding
//    private var mediaPlayer: MediaPlayer? = null
//    private var isPlaying = false
//    private val handler = Handler(Looper.getMainLooper())
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityPlayerBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        val track = intent.getParcelableExtra<Track>("TRACK_EXTRA")
//        if (track != null) {
//            displayTrackInfo(track)
//            setupMediaPlayer(track.previewUrl)
//        } else {
//
//        }
//
//        binding.buttonPlayTrack.setOnClickListener {
//            togglePlayback()
//        }
//
//        binding.tbPlayer.setNavigationOnClickListener {
//            finish()
//        }
//    }
//
//    private fun displayTrackInfo(track: Track) {
//        with(binding) {
//            tvTrackName.text = track.trackName
//            tvArtistName.text = track.artistName
//            trackDuration.text = formatTime(track.trackTimeMillis)
//            trackAlbum.text = track.trackAlbum
//            trackReleaseDate.text = track.releaseDate.substring(0, 4)
//            trackGenre.text = track.genre
//            trackCountry.text = track.trackCountry
//
//            Glide.with(this@PlayerActivity).load(track.artworkUrl100.replace("100x100", "600x600"))
//                .into(trackCover)
//        }
//    }
//
//    private fun setupMediaPlayer(previewUrl: String?) {
//        if (previewUrl.isNullOrEmpty()) {
//
//            return
//        }
//        mediaPlayer = MediaPlayer().apply {
//            setDataSource(previewUrl)
//            prepareAsync()
//            setOnPreparedListener {
//
//            }
//            setOnCompletionListener {
//                resetPlaybackState()
//            }
//        }
//    }
//
//    private fun togglePlayback() {
//        mediaPlayer?.let {
//            if (isPlaying) {
//                it.pause()
//                isPlaying = false
//                updatePlayButton()
//                stopTimer()
//            } else {
//                it.start()
//                isPlaying = true
//                updatePlayButton()
//                startTimer()
//            }
//        }
//    }
//
//    private fun updatePlayButton() {
//        val playIcon = if (isPlaying) {
//            R.drawable.button_pause_track
//        } else {
//            R.drawable.button_play_track
//        }
//        binding.buttonPlayTrack.setImageResource(playIcon)
//    }
//
//    private fun startTimer() {
//        handler.post(object : Runnable {
//            override fun run() {
//                mediaPlayer?.let {
//                    if (isPlaying) {
//                        val currentPosition = it.currentPosition / 1000
//                        val minutes = currentPosition / 60
//                        val seconds = currentPosition % 60
//                        binding.currentTime.text = String.format("%02d:%02d", minutes, seconds)
//                        handler.postDelayed(this, 1000)
//                    }
//                }
//            }
//        })
//    }
//
//    private fun stopTimer() {
//        handler.removeCallbacksAndMessages(null)
//    }
//
//    private fun resetPlaybackState() {
//        isPlaying = false
//        updatePlayButton()
//        binding.currentTime.text = "00:00"
//        stopTimer()
//    }
//
//    private fun formatTime(milliseconds: Int): String {
//        val minutes = milliseconds / 1000 / 60
//        val seconds = (milliseconds / 1000) % 60
//        return String.format("%02d:%02d", minutes, seconds)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaPlayer?.release()
//        mediaPlayer = null
//        stopTimer()
//    }
//}

class PlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlayerBinding
    private val playbackInteractor = PlaybackInteractor()
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getParcelableExtra<Track>("TRACK_EXTRA")
        track?.let {
            displayTrackInfo(it)
            playbackInteractor.setup(it.previewUrl) {
                resetPlaybackState()
            }
        }

        binding.buttonPlayTrack.setOnClickListener {
            togglePlayback()
        }

        binding.tbPlayer.setNavigationOnClickListener {
            onBackPressed() // Возвращение назад с остановкой воспроизведения
        }
    }

    override fun onBackPressed() {
        playbackInteractor.stop() // Остановка воспроизведения при выходе
        super.onBackPressed()
    }

    private fun displayTrackInfo(track: Track) {
        with(binding) {
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            trackDuration.text = formatTime(track.trackTimeMillis)
            trackAlbum.text = track.trackAlbum.ifEmpty { "Unknown Album" }
            trackReleaseDate.text = track.releaseDate.substring(0, 4)
            trackGenre.text = track.genre.ifEmpty { "Unknown Genre" }
            trackCountry.text = track.trackCountry.ifEmpty { "Unknown Country" }

            Glide.with(this@PlayerActivity)
                .load(track.artworkUrl100.replace("100x100", "600x600"))
                .into(trackCover)
        }
    }

    private fun togglePlayback() {
        if (playbackInteractor.isPlaying()) {
            playbackInteractor.pause {
                updatePlayButton(isPlaying = false)
                stopTimer()
            }
        } else {
            playbackInteractor.play {
                updatePlayButton(isPlaying = true)
                startTimer()
            }
        }
    }

    private fun startTimer() {
        handler.post(object : Runnable {
            override fun run() {
                if (playbackInteractor.isPlaying()) {
                    val currentPosition = playbackInteractor.getCurrentPosition() / 1000
                    val minutes = currentPosition / 60
                    val seconds = currentPosition % 60
                    binding.currentTime.text = String.format("%02d:%02d", minutes, seconds)
                    handler.postDelayed(this, 1000)
                }
            }
        })
    }

    private fun stopTimer() {
        handler.removeCallbacksAndMessages(null)
    }

    private fun resetPlaybackState() {
        updatePlayButton(isPlaying = false)
        binding.currentTime.text = "00:00"
        stopTimer()
    }

    private fun updatePlayButton(isPlaying: Boolean) {
        binding.buttonPlayTrack.setImageResource(
            if (isPlaying) R.drawable.button_pause_track else R.drawable.button_play_track
        )
    }

    private fun formatTime(milliseconds: Int): String {
        val minutes = milliseconds / 1000 / 60
        val seconds = (milliseconds / 1000) % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
