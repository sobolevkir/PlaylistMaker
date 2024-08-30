package com.sobolevkir.playlistmaker.playlists.domain.impl

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsInteractor
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsRepository
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistsInteractorImpl(private val repository: PlaylistsRepository) : PlaylistsInteractor {

    override suspend fun createPlaylist(playlist: Playlist) {
        repository.createPlaylist(playlist)
    }

    override fun getPlaylistNames(): Flow<List<String>> =
        repository.getPlaylistNames()

    override fun getPlaylists(): Flow<List<Playlist>> =
        repository.getPlaylists()

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Int =
        repository.addTrackToPlaylist(track, playlist)

    override fun saveCoverToPrivateStorage(uri: String): String =
        repository.saveCoverToPrivateStorage(uri)

}