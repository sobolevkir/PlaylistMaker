package com.sobolevkir.playlistmaker.media.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sobolevkir.playlistmaker.media.ui.model.FavoritesState

class FavoritesViewModel : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    init {
        stateLiveData.postValue(FavoritesState.NothingFound)
    }

}