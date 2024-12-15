import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.MediaPlayerRepository

class AndroidMediaPlayerRepository : MediaPlayerRepository {
    private var mediaPlayer: MediaPlayer? = null
    private var isPlaying = false

    override fun setup(url: String, onCompletion: () -> Unit) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(url)
            setOnPreparedListener { start() }
            setOnCompletionListener {
                this@AndroidMediaPlayerRepository.isPlaying = false
                onCompletion()
            }
            prepareAsync()
        }
    }

    override fun play(onPlay: () -> Unit) {
        mediaPlayer?.let {
            it.start()
            isPlaying = true
            onPlay()
        }
    }

    override fun pause(onPause: () -> Unit) {
        mediaPlayer?.let {
            it.pause()
            isPlaying = false
            onPause()
        }
    }

    override fun stop() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }

    override fun isPlaying(): Boolean = isPlaying

    override fun getCurrentPosition(): Long {
        return mediaPlayer?.currentPosition?.toLong() ?: 0L
    }
}
