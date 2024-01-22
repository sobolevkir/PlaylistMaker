package com.sobolevkir.playlistmaker.domain.api

import com.sobolevkir.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTrack(searchQueryText: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(tracksFound: List<Track>?)
    }
}