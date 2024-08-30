package com.sobolevkir.playlistmaker.playlists.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsInteractor
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePlaylistViewModel(private val playlistInteractor: PlaylistsInteractor) : ViewModel() {

    private var playlistNames: List<String> = emptyList()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            playlistInteractor
                .getPlaylistNames()
                .collect { playlistNames = it }
        }
    }

    fun createPlaylist(name: String, description: String, strCoverUri: String) {
        val playlist = Playlist(
            name = name,
            description = description,
            coverUri = playlistInteractor.saveCoverToPrivateStorage(strCoverUri)
        )
        viewModelScope.launch(Dispatchers.IO) { playlistInteractor.createPlaylist(playlist) }
    }

    fun isNameDuplicated(playlistName: String): Boolean {
        return playlistNames.contains(playlistName) == true
    }


}