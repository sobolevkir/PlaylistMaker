package com.sobolevkir.playlistmaker.tracklist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobolevkir.playlistmaker.R

class TrackListAdapter() : RecyclerView.Adapter<TrackListViewHolder>() {
    var tracks = mutableListOf<Track>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.tracklist_item_layout, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(tracks[position])
    }

    override fun getItemCount() = tracks.size

}