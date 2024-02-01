package com.sobolevkir.playlistmaker.domain.api

interface PlayerInteractor {

    fun preparePlayer(preparedConsumer: () -> Unit, completionConsumer: () -> Unit)

    fun playbackControl(startingConsumer: () -> Unit, pausingConsumer: () -> Unit)

    fun startPlayer()

    fun pausePlayer()

    fun isPlaying(): Boolean

    fun releasePlayer()

    fun getCurrentPosition(): String

}