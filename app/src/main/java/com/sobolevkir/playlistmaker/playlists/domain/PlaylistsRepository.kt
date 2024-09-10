package com.sobolevkir.playlistmaker.playlists.domain

import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistsRepository {

    suspend fun createPlaylist(name: String, description: String, strCoverUri: String)

    suspend fun removePlaylist(playlistId: Long): Int

    suspend fun updatePlaylistInfo(
        playlistId: Long, name: String,
        description: String, strCoverUri: String
    )

    fun getPlaylistNames(): Flow<List<String>>

    fun getPlaylist(playlistId: Long): Flow<Playlist>

    fun getPlaylists(): Flow<List<Playlist>>

    suspend fun addTrackToPlaylist(track: Track, playlistId: Long): Int

    suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Int

    fun getTracksFromPlaylist(playlistId: Long): Flow<List<Track>>

}