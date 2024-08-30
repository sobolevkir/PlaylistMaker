package com.sobolevkir.playlistmaker.playlists.data.impl

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import com.sobolevkir.playlistmaker.common.data.AppDatabase
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.playlists.data.converter.PlaylistDbConverter
import com.sobolevkir.playlistmaker.playlists.domain.PlaylistsRepository
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import kotlinx.coroutines.flow.Flow
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

    override suspend fun createPlaylist(playlist: Playlist) {
        appDatabase.getPlaylistDao().insertPlaylist(playlistDbConverter.convert(playlist))
    }

    override fun getPlaylistNames(): Flow<List<String>> =
        appDatabase.getPlaylistDao().getPlaylistNames()

    override fun getPlaylists(): Flow<List<Playlist>> =
        appDatabase.getPlaylistDao().getPlaylists()
            .map { playlists -> playlists.map { playlistDbConverter.convert(it) } }

    override suspend fun addTrackToPlaylist(track: Track, playlist: Playlist): Int {
        appDatabase.getTrackFromPlaylistDao().insertTrack(playlistDbConverter.convert(track))
        val updatedPlaylist = playlist.copy(trackIds = (playlist.trackIds + track.trackId))
        return appDatabase.getPlaylistDao()
            .updatePlaylist(playlistDbConverter.convert(updatedPlaylist))
    }

    override fun saveCoverToPrivateStorage(uri: String): String {
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
            Log.e("Error", "Failed to save the cover image!")
        }
        return ""
    }

    companion object {
        private const val COVERS_DIR = "playlist_covers"
    }

}