package com.sobolevkir.playlistmaker.media.presentation.model

import com.sobolevkir.playlistmaker.common.domain.model.Track

sealed interface FavoritesState {

    object NothingFound : FavoritesState

    data class Content(
        val tracks: List<Track>
    ) : FavoritesState

}