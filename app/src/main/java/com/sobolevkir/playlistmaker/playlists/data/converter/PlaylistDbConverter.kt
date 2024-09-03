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

    fun convert(playlist: PlaylistEntity): Playlist {
        return Playlist(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverUri = playlist.coverUri,
            trackIds = gson.fromJson(playlist.trackIds, Array<Long>::class.java).toList()
        )
    }

    fun convert(track: Track): TrackFromPlaylistEntity {
        return TrackFromPlaylistEntity(
            track.trackId,
            track.trackName,
            track.artistName,
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

}