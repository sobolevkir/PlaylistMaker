package com.sobolevkir.playlistmaker.player.presentation

sealed interface PlayerState {

    data object Default : PlayerState
    data object Prepared : PlayerState

    data class Playing(val progress: String) : PlayerState
    data class Paused(val progress: String) : PlayerState

}