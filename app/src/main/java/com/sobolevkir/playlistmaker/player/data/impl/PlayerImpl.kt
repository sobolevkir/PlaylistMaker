package com.sobolevkir.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.sobolevkir.playlistmaker.player.domain.Player
import com.sobolevkir.playlistmaker.player.domain.model.PlayerState
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerImpl(private val mediaPlayer: MediaPlayer) : Player {

    private var playerState = PlayerState.DEFAULT

    override fun preparePlayer(previewUrl: String, consumer: Player.Consumer) {
        if (previewUrl.isNotEmpty()) try {
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = PlayerState.PREPARED
                consumer.consume(playerState)
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerState.PREPARED
                consumer.consume(playerState)
            }
            mediaPlayer.setOnErrorListener { _, _, _ ->
                playerState = PlayerState.ERROR
                consumer.consume(playerState)
                true
            }
        } catch (ex: Exception) {
            return
        }
    }

    override fun startPlayer(consumer: Player.Consumer) {
        mediaPlayer.start()
        playerState = PlayerState.PLAYING
        consumer.consume(playerState)
    }

    override fun pausePlayer(consumer: Player.Consumer) {
        mediaPlayer.pause()
        playerState = PlayerState.PAUSED
        consumer.consume(playerState)
    }

    override fun resetPlayer() {
        if (playerState != PlayerState.DEFAULT) {
            mediaPlayer.reset()
            playerState = PlayerState.DEFAULT
        }
    }

    override fun getCurrentPosition(): String = SimpleDateFormat(
        "mm:ss", Locale.getDefault()
    ).format(mediaPlayer.currentPosition)

}