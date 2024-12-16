import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.IPlaybackInteractor


class MediaPlayerRepository : IPlaybackInteractor {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    override fun setup(url: String, onComplete: () -> Unit) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener {
                onComplete()
            }
            setOnCompletionListener {
                this@MediaPlayerRepository.isPlaying = false
                onComplete()
            }
            prepareAsync()
        }
    }


    override fun play(onComplete: () -> Unit) {
        mediaPlayer?.let {
            it.start()
            isPlaying = true
            onComplete()
        }
    }

    override fun pause(onComplete: () -> Unit) {
        mediaPlayer?.let {
            it.pause()
            isPlaying = false
            onComplete()
        }
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer?.currentPosition?.toLong() ?: 0L
    }

    override fun togglePlayback(onPlay: () -> Unit, onPause: () -> Unit) {
        if (isPlaying) {
            pause(onPause)
        } else {
            play(onPlay)
        }
    }

    override fun getCurrentTimeFormatted(): String {
        val currentPosition = getCurrentPosition() / 1000
        val minutes = currentPosition / 60
        val seconds = currentPosition % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}

