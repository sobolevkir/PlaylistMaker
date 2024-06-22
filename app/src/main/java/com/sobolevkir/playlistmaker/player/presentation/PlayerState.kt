package com.sobolevkir.playlistmaker.player.presentation

sealed class PlayerState(val progress: String) {

    class Default(progress: String) : PlayerState(progress)

    class Prepared(progress: String) : PlayerState(progress)

    class Playing(progress: String) : PlayerState(progress)

    class Paused(progress: String) : PlayerState(progress)

}