package com.sobolevkir.playlistmaker.playlists.domain

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun createPlaylist(playlist: Playlist)

    fun getPlaylistNames(): Flow<List<String>>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Int

    fun saveCoverToPrivateStorage(uri: String): String

}