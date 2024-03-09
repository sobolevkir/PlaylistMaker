package com.sobolevkir.playlistmaker.player.domain

import com.sobolevkir.playlistmaker.player.domain.model.PlayerState

interface Player {
    fun preparePlayer(previewUrl: String, consumer: Consumer)
    fun startPlayer(consumer: Consumer)
    fun pausePlayer(consumer: Consumer)
    fun resetPlayer()
    fun getCurrentPosition(): String
    fun interface Consumer {
        fun consume(state: PlayerState)
    }
}