package com.sobolevkir.playlistmaker.playlists.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import androidx.core.net.toUri
import com.sobolevkir.playlistmaker.common.data.AppDatabase
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.playlists.data.converter.PlaylistDbConverter
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsRepository
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlaylistsRepositoryImpl(
    private val context: Context,
    private val appDatabase: AppDatabase,
    private val playlistDbConverter: PlaylistDbConverter
) : PlaylistsRepository {

    override suspend fun createPlaylist(name: String, description: String, strCoverUri: String) {
        val newPlaylist = Playlist(
            name = name, description = description,
            coverUri = saveCoverToPrivateStorage(strCoverUri)
        )
        appDatabase.getPlaylistDao().insertPlaylist(playlistDbConverter.convert(newPlaylist))
    }

    override fun getPlaylistNames(): Flow<List<String>> =
        appDatabase.getPlaylistDao().getPlaylistNames()

    override fun getPlaylist(playlistId: Long): Flow<Playlist> =
        appDatabase.getPlaylistDao().getPlaylistById(playlistId)
            .map { playlistEntity -> playlistDbConverter.convert(playlistEntity) }

    override fun getPlaylists(): Flow<List<Playlist>> =
        appDatabase.getPlaylistDao().getPlaylists()
            .map { playlistEntities -> playlistEntities.map { playlistDbConverter.convert(it) } }

    override suspend fun addTrackToPlaylist(track: Track, playlistId: Long): Int {
        appDatabase.getTrackFromPlaylistDao().insertTrack(playlistDbConverter.convert(track))
        val playlist = getPlaylist(playlistId).first()
        val updatedPlaylist = playlist.copy(trackIds = (playlist.trackIds + track.trackId))
        return appDatabase.getPlaylistDao()
            .updatePlaylist(playlistDbConverter.convert(updatedPlaylist))
    }

    override suspend fun removeTrackFromPlaylist(trackId: Long, playlistId: Long): Int {
        val playlist = getPlaylist(playlistId).first()
        if (trackId !in playlist.trackIds) return 0
        val updatedPlaylist = playlist.copy(trackIds = playlist.trackIds - trackId)
        val result = appDatabase.getPlaylistDao()
            .updatePlaylist(playlistDbConverter.convert(updatedPlaylist))
        if (getPlaylists().first().none { trackId in it.trackIds }) {
            appDatabase.getTrackFromPlaylistDao().removeTrack(trackId)
        }
        return result
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getTracksFromPlaylist(playlistId: Long): Flow<List<Track>> =
        appDatabase.getPlaylistDao()
            .getPlaylistById(playlistId)
            .flatMapLatest { playlistEntity ->
                val trackIds = playlistDbConverter.convertTrackIdsFromJson(playlistEntity.trackIds)
                val tracksFlow = appDatabase.getTrackFromPlaylistDao()
                    .getTracksFromPlaylistByIds(trackIds)
                    .map { trackEntities ->
                        trackEntities.map { trackEntity ->
                            playlistDbConverter.convert(trackEntity)
                        }
                    }
                val favoriteTracksFlow = getFavoriteTrackIds()
                combine(tracksFlow, favoriteTracksFlow) { tracks, favoriteIds ->
                    tracks.withFavoriteFlag(favoriteIds)
                }
            }

    private fun getFavoriteTrackIds(): Flow<List<Long>> =
        appDatabase.getFavoriteTrackDao().getTrackIds()

    private fun List<Track>.withFavoriteFlag(favoriteTrackIds: List<Long>): List<Track> =
        this.map { track -> track.copy(isFavorite = favoriteTrackIds.contains(track.trackId)) }

    private fun saveCoverToPrivateStorage(uri: String): String {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), COVERS_DIR)
        if (!filePath.exists()) filePath.mkdirs()
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Cover_$timeStamp.jpg"
        val file = File(filePath, fileName)
        try {
            val inputStream = context.contentResolver.openInputStream(uri.toUri())
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
            outputStream.flush()
            outputStream.close()
            inputStream?.close()
            return file.toUri().toString()
        } catch (_: Exception) {
            return ""
        }
    }

    companion object {
        private const val COVERS_DIR = "playlist_covers"
    }

}