package com.msaggik.playlistmaker.media.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobolevkir.playlistmaker.databinding.LayoutPlaylistSmallListItemBinding
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist
import com.sobolevkir.playlistmaker.playlists.ui.adapters.PlaylistSmallListViewHolder

class PlaylistSmallListAdapter(private val onItemClick: (Playlist) -> Unit) :
    RecyclerView.Adapter<PlaylistSmallListViewHolder>() {

    val playlists = mutableListOf<Playlist>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSmallListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = LayoutPlaylistSmallListItemBinding.inflate(layoutInspector, parent, false)
        return PlaylistSmallListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlaylistSmallListViewHolder, position: Int) {
        with(holder) {
            bind(playlists[position])
            binding.root.setOnClickListener { onItemClick(playlists[position]) }
        }
    }

    override fun getItemCount() = playlists.size

}