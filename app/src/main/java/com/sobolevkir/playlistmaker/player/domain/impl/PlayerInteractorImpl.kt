package com.sobolevkir.playlistmaker.player.domain.impl

import com.sobolevkir.playlistmaker.player.domain.Player
import com.sobolevkir.playlistmaker.player.domain.PlayerInteractor

class PlayerInteractorImpl(private val repository: Player) : PlayerInteractor {

    override fun preparePlayer(consumer: Player.Consumer) =
        repository.preparePlayer(consumer)

    override fun startPlayer(consumer: Player.Consumer) = repository.startPlayer(consumer)

    override fun pausePlayer(consumer: Player.Consumer) = repository.pausePlayer(consumer)

    override fun releasePlayer() = repository.releasePlayer()

    override fun getCurrentPosition() = repository.getCurrentPosition()
}