package com.sobolevkir.playlistmaker.domain.impl

import com.sobolevkir.playlistmaker.domain.api.TracksInteractor
import com.sobolevkir.playlistmaker.domain.api.TracksRepository
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun searchTrack(searchQueryText: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTrack(searchQueryText))
        }
    }

}