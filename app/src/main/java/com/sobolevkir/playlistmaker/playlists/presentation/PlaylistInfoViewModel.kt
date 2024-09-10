package com.sobolevkir.playlistmaker.playlists.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.common.domain.presentation.SingleLiveEvent
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsInteractor
import com.sobolevkir.playlistmaker.playlists.domain.model.PlaylistWithTracks
import com.sobolevkir.playlistmaker.playlists.presentation.model.PlaylistInfoEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistInfoViewModel(
    private val playlistId: Long,
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val playlistWithTracksLiveData = MutableLiveData<PlaylistWithTracks>()
    private val resultSingleLiveEvent = SingleLiveEvent<PlaylistInfoEvent>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .getPlaylistWithTracks(playlistId)
                .collect { playlistWithTracks ->
                    playlistWithTracksLiveData.postValue(playlistWithTracks)
                }
        }
    }

    fun getPlaylistWithTracksLiveData(): LiveData<PlaylistWithTracks> = playlistWithTracksLiveData
    fun getResultSingleLiveEvent(): SingleLiveEvent<PlaylistInfoEvent> = resultSingleLiveEvent

    fun removeTrackFromPlaylist(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = playlistsInteractor.removeTrackFromPlaylist(track.trackId, playlistId)
            if (result > 0) {
                resultSingleLiveEvent.postValue(
                        PlaylistInfoEvent.TrackRemovedSuccess(track.trackName)
                )
            }
        }
    }

    fun removePlaylist() {
        val playlistName = playlistWithTracksLiveData.value?.name ?: ""
        viewModelScope.launch(Dispatchers.IO) {
            val result = playlistsInteractor.removePlaylist(playlistId)
            if (result > 0) {
                resultSingleLiveEvent.postValue(
                        PlaylistInfoEvent.PlaylistRemovedSuccess(playlistName)
                )
            }
        }
    }

    fun sharePlaylist() {
        val tracksNumber = playlistWithTracksLiveData.value?.tracksNumber ?: 0
        if(tracksNumber > 0) {
            viewModelScope.launch(Dispatchers.IO) {
                playlistsInteractor.sharePlaylist(playlistId)
            }
        } else {
            resultSingleLiveEvent.postValue(PlaylistInfoEvent.UnsuccessfulSharing)
        }
    }

}