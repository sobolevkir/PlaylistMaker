package com.sobolevkir.playlistmaker.playlists.domain.impl

import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.domain.ExternalNavigator
import com.sobolevkir.playlistmaker.common.domain.ResourceProvider
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsInteractor
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsRepository
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import com.sobolevkir.playlistmaker.playlists.domain.model.PlaylistWithTracks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistsInteractorImpl(
    private val repository: PlaylistsRepository,
    private val externalNavigator: ExternalNavigator,
    private val resourceProvider: ResourceProvider
) : PlaylistsInteractor {

    override suspend fun createPlaylist(name: String, description: String, strCoverUri: String) {
        repository.createPlaylist(name, description, strCoverUri)
    }

    override suspend fun removePlaylist(playlistId: Long): Int =
        repository.removePlaylist(playlistId)

    override suspend fun getPlaylist(playlistId: Long): Playlist =
        repository.getPlaylist(playlistId).first()

    override suspend fun updatePlaylistInfo(
        playlistId: Long, name: String,
        description: String, strCoverUri: String
    ) {
        repository.updatePlaylistInfo(playlistId, name, description, strCoverUri)
    }

    override fun getPlaylistNames(): Flow<List<String>> =
        repository.getPlaylistNames()

    override fun getPlaylists(): Flow<List<Playlist>> =
        repository.getPlaylists()

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long): Int =
        repository.addTrackToPlaylist(track, playlistId)

    override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Int =
        repository.removeTrackFromPlaylist(trackId, playlistId)

    override fun getPlaylistWithTracks(playlistId: Long): Flow<PlaylistWithTracks> {
        val playlistFlow = repository.getPlaylist(playlistId)
        val tracksFlow = repository.getTracksFromPlaylist(playlistId)

        return combine(playlistFlow, tracksFlow) { playlist, tracks ->
            val totalMinutesDuration = getTimeInMinutes(tracks.sumOf { it.trackTimeMillis })
            PlaylistWithTracks(
                id = playlist.id,
                name = playlist.name,
                description = playlist.description,
                coverUri = playlist.coverUri,
                tracks = tracks,
                totalMinutesDuration = totalMinutesDuration,
                tracksNumber = tracks.size
            )
        }
    }

    override suspend fun sharePlaylist(playlistId: Long) {
        val playlist = getPlaylistWithTracks(playlistId).first()
        val textToShare = buildString {
            appendLine(playlist.name)
            appendLine(playlist.description)
            appendLine(
                resourceProvider.getQuantityString(
                    R.plurals.tracks_count,
                    playlist.tracksNumber
                )
            )
            playlist.tracks.forEachIndexed { index, track ->
                appendLine("${index + 1}. ${track.artistName} - ${track.trackName} (${track.formattedTrackTime})")
            }
        }
        externalNavigator.shareText(textToShare)
    }

    private fun getTimeInMinutes(timeInMillis: Int): Int =
        if (timeInMillis > 0) {
            SimpleDateFormat("mm", Locale.getDefault()).format(timeInMillis).toInt()
        } else 0

}