package com.sobolevkir.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobolevkir.playlistmaker.media.ui.model.MediaState

class FavoritesViewModel : ViewModel() {

    private val stateLiveData = MutableLiveData<MediaState>()
    fun observeState(): LiveData<MediaState> = stateLiveData

    init {
        stateLiveData.postValue(MediaState.NothingFound)
    }

}