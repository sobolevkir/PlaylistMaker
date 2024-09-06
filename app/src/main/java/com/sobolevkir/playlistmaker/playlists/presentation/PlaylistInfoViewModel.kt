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
    private val resultSingleLiveEvent = SingleLiveEvent<Pair<PlaylistInfoEvent, String>>()

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
    fun getResultSingleLiveEvent(): SingleLiveEvent<Pair<PlaylistInfoEvent, String>> = resultSingleLiveEvent

    fun removeTrackFromPlaylist(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = playlistsInteractor.removeTrackFromPlaylist(track.trackId, playlistId)
            if (result > 0) {
                resultSingleLiveEvent.postValue(Pair(PlaylistInfoEvent.TrackRemovedSuccess, track.trackName))
            }
        }
    }

}