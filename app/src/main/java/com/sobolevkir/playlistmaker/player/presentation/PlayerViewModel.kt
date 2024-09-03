package com.sobolevkir.playlistmaker.player.presentation

import android.util.Log
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.domain.presentation.SingleLiveEvent
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesInteractor
import com.sobolevkir.playlistmaker.player.domain.PlayerInteractor
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsInteractor
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class PlayerViewModel(
    track: Track,
    private val playerInteractor: PlayerInteractor,
    private val favoritesInteractor: FavoritesInteractor,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel(), DefaultLifecycleObserver {

    private var timerJob: Job? = null
    private val playerStateLiveData = MutableLiveData<PlayerState>()
    private val currentTrackLiveData = MutableLiveData<Track>()
    private val playlistsLiveData = MutableLiveData<List<Playlist>>()
    private val addingResultSingleLiveEvent = SingleLiveEvent<Pair<Boolean, String>>()

    init {
        currentTrackLiveData.value = track
        playerInteractor.preparePlayer(track.previewUrl) { playerState ->
            playerStateLiveData.postValue(playerState)
            if (playerState !is PlayerState.Playing) timerJob?.cancel()
        }
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .getPlaylists()
                .collect { playlists ->
                    playlistsLiveData.postValue(playlists)
                }
        }
    }

    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveData
    fun getCurrentTrackLiveData(): LiveData<Track> = currentTrackLiveData
    fun getPlaylistsLiveData(): LiveData<List<Playlist>> = playlistsLiveData
    fun getAddingResultSingleLiveEvent(): SingleLiveEvent<Pair<Boolean, String>> =
        addingResultSingleLiveEvent

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
            viewModelScope.launch(Dispatchers.IO) {
                if (currentTrack.isFavorite) {
                    favoritesInteractor.removeTrackFromFavorites(currentTrack)
                } else {
                    favoritesInteractor.addTrackToFavorites(currentTrack)
                }
            }
            currentTrackLiveData.value = it.copy(isFavorite = !it.isFavorite)
        }
    }

    fun onPlaylistSelect(playlist: Playlist) {
        val currentTrack = currentTrackLiveData.value ?: Track()
        if (playlist.trackIds.contains(currentTrack.trackId)) {
            addingResultSingleLiveEvent.value = Pair(false, playlist.name)
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                val result = playlistsInteractor.addTrackToPlaylist(currentTrack, playlist)
                if (result.toInt() > 0) {
                    addingResultSingleLiveEvent.postValue(Pair(true, playlist.name))
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch(Dispatchers.IO) {
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