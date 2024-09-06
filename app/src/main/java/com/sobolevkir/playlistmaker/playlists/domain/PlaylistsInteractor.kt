package com.sobolevkir.playlistmaker.playlists.domain

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import com.sobolevkir.playlistmaker.playlists.domain.model.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow

interface PlaylistsInteractor {

    suspend fun createPlaylist(name: String, description: String, strCoverUri: String)

    fun getPlaylistNames(): Flow<List<String>>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlistId: Long): Int

    suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Int

    fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks>

}