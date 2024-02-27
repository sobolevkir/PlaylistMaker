package com.sobolevkir.playlistmaker.player.ui.viewmodel

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sobolevkir.playlistmaker.creator.Creator
import com.sobolevkir.playlistmaker.player.domain.model.PlayerState
import com.sobolevkir.playlistmaker.common.domain.model.Track

class PlayerViewModel(track: Track, application: Application) : AndroidViewModel(application),
    DefaultLifecycleObserver {

    private val playerInteractor = Creator.providePlayerInteractor(track.previewUrl)
    private val favoritesInteractor = Creator.provideFavoritesInteractor()
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
        playerInteractor.preparePlayer { state ->
            mainThreadHandler.removeCallbacks(updateCurrentPosition)
            progressLiveData.value = ""
            playerStateLiveData.value = state
            Log.d("vm-player", "preparePlayer, PlayerState: " + playerStateLiveData.value)
            if (playerStateLiveData.value == PlayerState.ERROR) playerInteractor.releasePlayer()
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
        playerInteractor.releasePlayer()
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
        fun getViewModelFactory(track: Track): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel(track, (this[APPLICATION_KEY] as Application))
            }
        }
    }
}