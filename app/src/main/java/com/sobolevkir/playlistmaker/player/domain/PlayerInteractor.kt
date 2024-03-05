package com.sobolevkir.playlistmaker.player.domain

interface PlayerInteractor {
    fun preparePlayer(previewUrl: String, consumer: Player.Consumer)
    fun startPlayer(consumer: Player.Consumer)
    fun pausePlayer(consumer: Player.Consumer)
    fun releasePlayer()
    fun getCurrentPosition(): String
}