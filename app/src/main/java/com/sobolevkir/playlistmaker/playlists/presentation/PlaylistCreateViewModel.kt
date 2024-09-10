package com.sobolevkir.playlistmaker.playlists.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class PlaylistCreateViewModel(private val playlistsInteractor: PlaylistsInteractor) : ViewModel() {

    private var playlistNames: List<String> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor
                .getPlaylistNames()
                .collect { playlistNames = it }
        }
    }

    open fun onSubmitButtonClick(name: String, description: String, strCoverUri: String) {
        viewModelScope.launch(Dispatchers.IO) {
            playlistsInteractor.createPlaylist(
                name,
                description,
                strCoverUri
            )
        }
    }

    open fun isNameDuplicated(playlistName: String): Boolean = playlistNames.contains(playlistName)

}