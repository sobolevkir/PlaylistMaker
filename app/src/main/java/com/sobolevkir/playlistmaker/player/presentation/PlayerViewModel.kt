package com.sobolevkir.playlistmaker.player.presentation

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesInteractor
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.player.domain.PlayerInteractor
import com.sobolevkir.playlistmaker.player.domain.model.PlayerState

class PlayerViewModel(
    track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel(), DefaultLifecycleObserver {

    private val playerStateLiveData = MutableLiveData<PlayerState>()
    private val progressLiveData = MutableLiveData<String>()
    private val currentTrackLiveData = MutableLiveData<Track>()

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private var updateCurrentPosition = object : Runnable {
        override fun run() {
            progressLiveData.value = playerInteractor.getCurrentPosition()
            mainThreadHandler.postDelayed(this, UPDATER_DELAY)
        }
    }

    init {
        playerInteractor.preparePlayer(track.previewUrl) { state ->
            mainThreadHandler.removeCallbacks(updateCurrentPosition)
            progressLiveData.value = ""
            playerStateLiveData.value = state
            if (playerStateLiveData.value == PlayerState.ERROR) playerInteractor.resetPlayer()
        }
        if (favoritesInteractor.isTrackFavorite(track.trackId)) {
            currentTrackLiveData.value = track.copy(isFavorite = true)
        } else {
            currentTrackLiveData.value = track.copy(isFavorite = false)
        }
    }

    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData
    fun getProgressLiveData(): LiveData<String> = progressLiveData
    fun getCurrentTrackLiveData(): LiveData<Track> = currentTrackLiveData

    fun playbackControl() {
        when (playerStateLiveData.value) {
            PlayerState.PLAYING -> {
                playerInteractor.pausePlayer { state ->
                    playerStateLiveData.value = state
                    mainThreadHandler.removeCallbacks(updateCurrentPosition)
                }
            }

            PlayerState.PREPARED, PlayerState.PAUSED -> {
                playerInteractor.startPlayer { state ->
                    playerStateLiveData.value = state
                    mainThreadHandler.postDelayed(updateCurrentPosition, UPDATER_DELAY)
                }
            }

            else -> {}
        }
    }

    fun onFavoriteButtonClick() {
        val changedTrack = currentTrackLiveData.value
        changedTrack?.let {
            if (it.isFavorite) {
                favoritesInteractor.removeTrackFromFavorites(it.trackId)
            } else {
                favoritesInteractor.addTrackToFavorites(it.trackId)
            }
            currentTrackLiveData.value = it.copy(isFavorite = !it.isFavorite)
        }
    }

    override fun onCleared() {
        mainThreadHandler.removeCallbacks(updateCurrentPosition)
        playerInteractor.resetPlayer()
    }

    override fun onPause(owner: LifecycleOwner) {
        if (playerStateLiveData.value == PlayerState.PLAYING) {
            playerInteractor.pausePlayer { state ->
                playerStateLiveData.value = state
                mainThreadHandler.removeCallbacks(updateCurrentPosition)
            }
        }
        super.onPause(owner)
    }

    companion object {
        private const val UPDATER_DELAY = 200L
    }
}