package com.sobolevkir.playlistmaker.playlists.data.converter

import com.google.gson.Gson
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.playlists.data.entity.PlaylistEntity
import com.sobolevkir.playlistmaker.playlists.data.entity.TrackFromPlaylistEntity
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist

class PlaylistDbConverter(private val gson: Gson) {

    fun convert(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            trackIds = gson.toJson(playlist.trackIds)
        )
    }

    fun convert(playlistEntity: PlaylistEntity): Playlist {
        return Playlist(
            id = playlistEntity.id,
            name = playlistEntity.name,
            description = playlistEntity.description,
            coverUri = playlistEntity.coverUri,
            trackIds = convertTrackIdsFromJson(playlistEntity.trackIds)
        )
    }

    fun convert(track: Track): TrackFromPlaylistEntity {
        return TrackFromPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.formattedTrackTime,
            track.artworkUrl100,
            track.artworkUrl512,
            track.collectionName,
            track.releaseYear,
            track.primaryGenreName,
            track.country,
            track.previewUrl
        )
    }

    fun convert(trackEntity: TrackFromPlaylistEntity): Track {
        return Track(
            trackEntity.trackId,
            trackEntity.trackName,
            trackEntity.artistName,
            trackEntity.trackTimeMillis,
            trackEntity.formattedTrackTime,
            trackEntity.artworkUrl100,
            trackEntity.artworkUrl512,
            trackEntity.collectionName,
            trackEntity.releaseYear,
            trackEntity.primaryGenreName,
            trackEntity.country,
            trackEntity.previewUrl
        )
    }

    fun convertTrackIdsFromJson(trackIds: String): List<Long> =
        if (trackIds.isNotEmpty()) {
            gson.fromJson(trackIds, Array<Long>::class.java).toList()
        } else listOf()

}