package com.sobolevkir.playlistmaker.player.data.impl

import android.media.MediaPlayer
import android.util.Log
import com.sobolevkir.playlistmaker.player.domain.model.PlayerState
import com.sobolevkir.playlistmaker.player.domain.Player
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerImpl(private val previewUrl: String) : Player {

    private var mediaPlayer = MediaPlayer()
    private var playerState = PlayerState.DEFAULT

    override fun preparePlayer(consumer: Player.Consumer) {
        Log.d("player", "preparePlayer()")
        if (previewUrl.isNotEmpty()) try {
            mediaPlayer.setDataSource(previewUrl)
            mediaPlayer.prepareAsync()
            mediaPlayer.setOnPreparedListener {
                playerState = PlayerState.PREPARED
                Log.d("player", "STATE: $playerState")
                consumer.consume(playerState)
            }
            mediaPlayer.setOnCompletionListener {
                Log.d("player", "STATE: $playerState")
                playerState = PlayerState.PREPARED
                consumer.consume(playerState)
            }
            mediaPlayer.setOnErrorListener { _, errorType, _ ->
                playerState = PlayerState.ERROR
                Log.d("player", "ERROR: $errorType")
                consumer.consume(playerState)
                true
            }
        } catch (ex: Exception) {
            Log.d("player", "exception: $ex")
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

    override fun releasePlayer() {
        Log.d("player", "releasePlayer()")
        if (playerState != PlayerState.DEFAULT) {
            mediaPlayer.release()
            Log.d("player", "PLAYER RELEASED!")
            playerState = PlayerState.DEFAULT
        }
    }

    override fun getCurrentPosition(): String = SimpleDateFormat(
        "mm:ss", Locale.getDefault()
    ).format(mediaPlayer.currentPosition)

}