package com.sobolevkir.playlistmaker.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.favorites.domain.FavoritesInteractor
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class FavoritesViewModel(private val favoritesInteractor: FavoritesInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoritesState>()
    fun observeState(): LiveData<FavoritesState> = stateLiveData

    init {
        fillData()
    }

    private fun fillData() {
        renderState(FavoritesState.Loading)
        favoritesInteractor
            .getFavoriteTracks()
            .onEach { favorites ->
                processResult(favorites)
            }
            .launchIn(viewModelScope)
    }

    private fun processResult(favorites: List<Track>) {
        if (favorites.isEmpty()) {
            renderState(FavoritesState.Empty)
        } else {
            renderState(FavoritesState.Content(favorites))
        }
    }

    private fun renderState(state: FavoritesState) = stateLiveData.postValue(state)
}