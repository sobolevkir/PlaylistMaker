package com.sobolevkir.playlistmaker.data.repository

import android.media.MediaPlayer
import com.sobolevkir.playlistmaker.domain.api.PlayerRepository
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerRepositoryImpl(private val trackPreviewUrl: String?) : PlayerRepository {

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    override fun preparePlayer(preparedConsumer: () -> Unit, completionConsumer: () -> Unit) {
        mediaPlayer.setDataSource(trackPreviewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            preparedConsumer.invoke()
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            completionConsumer.invoke()
            playerState = STATE_PREPARED
        }
    }

    override fun playbackControl(startingConsumer: () -> Unit, pausingConsumer: () -> Unit) {
        when (playerState) {
            STATE_PLAYING -> {
                pausingConsumer.invoke()
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
                startingConsumer.invoke()
            }
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun isPlaying() = playerState == STATE_PLAYING

    override fun releasePlayer() = mediaPlayer.release()

    override fun getCurrentPosition(): String = SimpleDateFormat(
        "mm:ss", Locale.getDefault()
    ).format(mediaPlayer.currentPosition)

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
    }

}