package com.sobolevkir.playlistmaker.domain.impl

import com.sobolevkir.playlistmaker.domain.api.PlayerInteractor
import com.sobolevkir.playlistmaker.domain.api.PlayerRepository

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {

    override fun preparePlayer(preparedConsumer: () -> Unit, completionConsumer: () -> Unit) =
        repository.preparePlayer(preparedConsumer, completionConsumer)

    override fun playbackControl(startingConsumer: () -> Unit, pausingConsumer: () -> Unit) =
        repository.playbackControl(startingConsumer, pausingConsumer)

    override fun startPlayer() = repository.startPlayer()

    override fun pausePlayer() = repository.pausePlayer()

    override fun isPlaying() = repository.isPlaying()

    override fun releasePlayer() = repository.releasePlayer()

    override fun getCurrentPosition() = repository.getCurrentPosition()
}