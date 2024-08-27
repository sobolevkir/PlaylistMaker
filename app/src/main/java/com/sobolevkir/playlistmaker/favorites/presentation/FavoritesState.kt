package com.sobolevkir.playlistmaker.favorites.presentation

import com.sobolevkir.playlistmaker.common.domain.model.Track

sealed interface FavoritesState {

    data object Loading: FavoritesState
    data object Empty : FavoritesState

    data class Content(
        val tracks: List<Track>
    ) : FavoritesState

}