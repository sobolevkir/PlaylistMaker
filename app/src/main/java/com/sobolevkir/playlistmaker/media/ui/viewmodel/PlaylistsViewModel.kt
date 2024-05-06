package com.sobolevkir.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobolevkir.playlistmaker.media.ui.model.PlaylistsState

class PlaylistsViewModel : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsState>()
    fun observeState(): LiveData<PlaylistsState> = stateLiveData

    init {
        stateLiveData.postValue(PlaylistsState.NothingFound)
    }

}