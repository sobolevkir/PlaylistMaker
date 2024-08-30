package com.msaggik.playlistmaker.media.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobolevkir.playlistmaker.databinding.LayoutPlaylistLargeListItemBinding
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import com.sobolevkir.playlistmaker.playlists.ui.adapters.PlaylistLargeListViewHolder

class PlaylistLargeListAdapter(private val onItemClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistLargeListViewHolder>() {

    val playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistLargeListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = LayoutPlaylistLargeListItemBinding.inflate(layoutInspector, parent, false)
        return PlaylistLargeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistLargeListViewHolder, position: Int) {
        with(holder) {
            bind(playlists[position])
            binding.root.setOnClickListener { onItemClick(playlists[position]) }
        }
    }

    override fun getItemCount() = playlists.size

}