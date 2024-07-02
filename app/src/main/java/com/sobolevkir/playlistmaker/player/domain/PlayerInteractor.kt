package com.sobolevkir.playlistmaker.player.domain

import com.sobolevkir.playlistmaker.player.presentation.PlayerState

interface PlayerInteractor {
    fun preparePlayer(previewUrl: String, consumer: Consumer)
    fun startPlayer(consumer: Consumer)
    fun pausePlayer(consumer: Consumer)
    fun isPlaying(): Boolean
    fun resetPlayer()
    fun getCurrentPlayerPosition(): String
    fun interface Consumer {
        fun consume(playerState: PlayerState)
    }
}