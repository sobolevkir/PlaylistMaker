package com.sobolevkir.playlistmaker.playlists.ui.adapters

import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.databinding.LayoutPlaylistSmallListItemBinding
import com.sobolevkir.playlistmaker.playlists.domain.model.Playlist

class PlaylistSmallListViewHolder(val binding: LayoutPlaylistSmallListItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(playlist: Playlist) {
        val cornerRadius = binding.root.resources.getDimensionPixelSize(R.dimen.radius_small)
        Glide.with(itemView)
            .load(playlist.coverUri.toUri())
            .transform(CenterCrop(), RoundedCorners(cornerRadius))
            .placeholder(R.drawable.cover_placeholder)
            .into(binding.ivCoverPlaylist)
        binding.tvPlaylistName.text = playlist.name
        binding.tvTracksNumber.text = binding.root.context.resources.getQuantityString(
            R.plurals.tracks_count,
            playlist.trackIds.size,
            playlist.trackIds.size
        )
    }
}