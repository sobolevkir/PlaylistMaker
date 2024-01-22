package com.sobolevkir.playlistmaker.presentation.ui.track_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobolevkir.playlistmaker.databinding.LayoutTracklistItemBinding
import com.sobolevkir.playlistmaker.domain.models.Track


class TrackListAdapter(
    private val tracks: MutableList<Track>,
    private val onItemClick: (Track) -> Unit
) : RecyclerView.Adapter<TrackListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val binding = LayoutTracklistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return TrackListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        with(holder) {
            bind(tracks[position])
            binding.root.setOnClickListener { onItemClick(tracks[position]) }
        }
    }

    override fun getItemCount() = tracks.size

}