import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.IPlaybackInteractor

class MediaPlayerRepository(private val mediaPlayer: MediaPlayer) : IPlaybackInteractor {
    private var isPlaying = false

    override fun setup(url: String, onComplete: () -> Unit) {
//        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.setOnPreparedListener {
            onComplete()
        }
        mediaPlayer.setOnCompletionListener {
            isPlaying = false
            onComplete()
        }
        mediaPlayer.prepareAsync()
    }

    override fun play(onComplete: () -> Unit) {
        if (!isPlaying) {
            mediaPlayer.start()
            isPlaying = true
            onComplete()
        }
    }

    override fun pause(onComplete: () -> Unit) {
        if (isPlaying) {
            mediaPlayer.pause()
            isPlaying = false
            onComplete()
        }
    }

    override fun stop() {
        if (isPlaying) {
            mediaPlayer.stop()
            isPlaying = false
        }
        mediaPlayer.reset()
    }

    override fun release() {
        if (isPlaying) {
            mediaPlayer.stop()
//            mediaPlayer.release()
        }
    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer.currentPosition.toLong()
    }

    override fun getCurrentTimeFormatted(): String {
        val currentPosition = getCurrentPosition() / 1000
        val minutes = currentPosition / 60
        val seconds = currentPosition % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun togglePlayback(onPlay: () -> Unit, onPause: () -> Unit) {
        if (isPlaying) {
            pause(onPause)
        } else {
            play(onPlay)
        }
    }
}

