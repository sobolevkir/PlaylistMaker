package com.sobolevkir.playlistmaker.playlists.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsInteractor
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistEditViewModel(
    private val playlistId: Long, private val playlistsInteractor: PlaylistsInteractor
) : PlaylistCreateViewModel(playlistsInteractor) {

    private val playlistLiveData = MutableLiveData<Playlist>()
    fun getPlaylistLiveData(): LiveData<Playlist> = playlistLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistLiveData.postValue(playlistsInteractor.getPlaylist(playlistId))
        }
    }

    override fun onSubmitButtonClick(name: String, description: String, strCoverUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.updatePlaylistInfo(
                playlistId, name, description, strCoverUri
            )
        }
    }

    override fun isNameDuplicated(playlistName: String): Boolean {
        return super.isNameDuplicated(playlistName) && (playlistLiveData.value?.name != playlistName)
    }

}