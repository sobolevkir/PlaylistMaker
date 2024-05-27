package com.sobolevkir.playlistmaker.search.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobolevkir.playlistmaker.databinding.LayoutTracklistItemBinding
import com.sobolevkir.playlistmaker.common.domain.model.Track


class TrackListAdapter(private val onItemClick: (Track) -> Unit) :
    RecyclerView.Adapter<TrackListViewHolder>() {

    val tracks = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val layoutInspector = LayoutInflater.from(parent.context)
        val binding = LayoutTracklistItemBinding.inflate(layoutInspector, parent, false)
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