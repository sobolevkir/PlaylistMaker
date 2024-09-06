package com.sobolevkir.playlistmaker.playlists.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistCreateViewModel(private val playlistInteractor: PlaylistsInteractor) : ViewModel() {

    private var playlistNames: List<String> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor
                .getPlaylistNames()
                .collect { playlistNames = it }
        }
    }

    fun createPlaylist(name: String, description: String, strCoverUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor.createPlaylist(
                name,
                description,
                strCoverUri
            )
        }
    }

    fun isNameDuplicated(playlistName: String): Boolean = playlistNames.contains(playlistName)


}