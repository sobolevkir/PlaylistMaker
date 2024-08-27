package com.sobolevkir.playlistmaker.player.presentation

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesInteractor
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.player.domain.PlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor
) : ViewModel(), DefaultLifecycleObserver {

    private var timerJob: Job? = null
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    private val currentTrackLiveData = MutableLiveData<Track>()

    init {
        playerInteractor.preparePlayer(track.previewUrl) { playerState ->
            playerStateLiveData.postValue(playerState)
            if (playerState !is PlayerState.Playing) timerJob?.cancel()
        }
        currentTrackLiveData.postValue(track)
    }

    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData
    fun getCurrentTrackLiveData(): LiveData<Track> = currentTrackLiveData

    fun playbackControl() {
        when (playerStateLiveData.value) {
            is PlayerState.Playing -> pausePlayer()
            is PlayerState.Prepared, is PlayerState.Paused -> startPlayer()
            else -> {}
        }
    }

    fun onFavoriteButtonClick() {
        val currentTrack = currentTrackLiveData.value
        currentTrack?.let {
            viewModelScope.launch {
                if (currentTrack.isFavorite) {
                    favoritesInteractor.removeTrackFromFavorites(currentTrack)
                } else {
                    favoritesInteractor.addTrackToFavorites(currentTrack)
                }
            }
            currentTrackLiveData.value = it.copy(isFavorite = !it.isFavorite)
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(UPDATER_DELAY)
                if (this.isActive) {
                    playerStateLiveData.postValue(
                        PlayerState.Playing(playerInteractor.getCurrentPlayerPosition())
                    )
                }
            }
        }
    }

    private fun startPlayer() {
        playerInteractor.startPlayer { playerState ->
            playerStateLiveData.postValue(playerState)
        }
        startTimer()
    }

    private fun pausePlayer() {
        timerJob?.cancel()
        playerInteractor.pausePlayer { playerState ->
            playerStateLiveData.postValue(playerState)
        }
    }

    override fun onPause(owner: LifecycleOwner) {
        if (playerStateLiveData.value is PlayerState.Playing) {
            pausePlayer()
        }
        super.onPause(owner)
    }

    override fun onCleared() {
        playerInteractor.resetPlayer()
    }

    companion object {
        private const val UPDATER_DELAY = 100L
    }
}