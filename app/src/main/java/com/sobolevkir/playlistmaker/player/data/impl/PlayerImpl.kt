package com.sobolevkir.playlistmaker.player.data.impl

import android.media.MediaPlayer
import com.sobolevkir.playlistmaker.player.data.model.PlayerStatus
import com.sobolevkir.playlistmaker.player.domain.Player
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerImpl(private val mediaPlayer: MediaPlayer) : Player {

    private var playerState = PlayerStatus.DEFAULT

    override fun preparePlayer(previewUrl: String, consumer: Player.Consumer) {
        if (previewUrl.isNotEmpty()) try {
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = PlayerStatus.PREPARED
                consumer.consume(playerState)
            }
            mediaPlayer.setOnErrorListener { player, _, _ ->
                playerState = PlayerStatus.ERROR
                consumer.consume(playerState)
                player.reset()
                true
            }
            mediaPlayer.setOnCompletionListener {
                playerState = PlayerStatus.PREPARED
                consumer.consume(playerState)
            }
        } catch (ex: Exception) {
            playerState = PlayerStatus.ERROR
            return
        }
    }

    override fun startPlayer(consumer: Player.Consumer) {
        mediaPlayer.start()
        playerState = PlayerStatus.PLAYING
        consumer.consume(playerState)
    }

    override fun pausePlayer(consumer: Player.Consumer) {
        mediaPlayer.pause()
        playerState = PlayerStatus.PAUSED
        consumer.consume(playerState)
    }

    override fun isPlaying(): Boolean = mediaPlayer.isPlaying

    override fun resetPlayer() {
        if (playerState != PlayerStatus.DEFAULT) {
            mediaPlayer.reset()
            playerState = PlayerStatus.DEFAULT
        }
    }

    override fun getCurrentPlayerPosition(): String = SimpleDateFormat(
        "mm:ss", Locale.getDefault()
    ).format(mediaPlayer.currentPosition)

}