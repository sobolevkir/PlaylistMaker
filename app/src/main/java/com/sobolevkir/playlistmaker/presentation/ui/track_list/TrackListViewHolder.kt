package com.sobolevkir.playlistmaker.presentation.ui.track_list

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.databinding.LayoutTracklistItemBinding
import com.sobolevkir.playlistmaker.domain.models.Track

class TrackListViewHolder(val binding: LayoutTracklistItemBinding): RecyclerView.ViewHolder(binding.root) {

    fun bind(track: Track) {
        binding.tvTrackName.text = track.trackName
        binding.tvArtistName.text = track.artistName
        binding.tvTrackTime.text = track.formattedTrackTime
        val cornerRadius = binding.root.resources.getDimensionPixelSize(R.dimen.radius_small)
        Glide.with(binding.root)
            .load(track.artworkUrl100)
            .centerInside()
            .placeholder(R.drawable.cover_placeholder)
            .transform(RoundedCorners(cornerRadius))
            .into(binding.ivAlbumCover)
        binding.tvArtistName.requestLayout()
    }

}