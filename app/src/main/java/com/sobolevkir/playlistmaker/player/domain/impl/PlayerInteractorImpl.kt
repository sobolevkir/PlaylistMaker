package com.sobolevkir.playlistmaker.player.domain.impl

import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.domain.ResourceProvider
import com.sobolevkir.playlistmaker.player.data.model.PlayerStatus
import com.sobolevkir.playlistmaker.player.domain.Player
import com.sobolevkir.playlistmaker.player.domain.PlayerInteractor
import com.sobolevkir.playlistmaker.player.presentation.PlayerState

class PlayerInteractorImpl(
    private val player: Player, private val resourceProvider: ResourceProvider
) : PlayerInteractor {

    override fun preparePlayer(previewUrl: String, consumer: PlayerInteractor.Consumer) =
        player.preparePlayer(previewUrl) { playerStatus ->
            consumer.consume(getPlayerState(playerStatus))
        }

    override fun startPlayer(consumer: PlayerInteractor.Consumer) =
        player.startPlayer { playerStatus ->
            consumer.consume(getPlayerState(playerStatus))
        }

    override fun pausePlayer(consumer: PlayerInteractor.Consumer) =
        player.pausePlayer { playerStatus ->
            consumer.consume(getPlayerState(playerStatus))
        }

    override fun isPlaying(): Boolean = player.isPlaying()

    override fun resetPlayer() = player.resetPlayer()

    override fun getCurrentPlayerPosition() = player.getCurrentPlayerPosition()

    private fun getPlayerState(playerStatus: PlayerStatus): PlayerState {
        return when (playerStatus) {
            PlayerStatus.PREPARED -> PlayerState.Prepared(
                resourceProvider.getString(R.string.hint_track_duration)
            )
            PlayerStatus.PLAYING -> PlayerState.Playing(player.getCurrentPlayerPosition())
            PlayerStatus.PAUSED -> PlayerState.Paused(player.getCurrentPlayerPosition())
            else -> PlayerState.Default(resourceProvider.getString(R.string.hint_track_duration))
        }
    }
}