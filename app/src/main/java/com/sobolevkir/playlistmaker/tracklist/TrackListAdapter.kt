package com.sobolevkir.playlistmaker.tracklist

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobolevkir.playlistmaker.PlayerActivity
import com.sobolevkir.playlistmaker.R


class TrackListAdapter(private val tracks: MutableList<Track>,
                       private val onTrackClickListener: OnTrackClickListener? = null
) : RecyclerView.Adapter<TrackListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackListViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.layout_tracklist_item, parent, false)
        return TrackListViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackListViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            onTrackClickListener?.onTrackClick(tracks[position])
            val playerIntent = Intent(it.context, PlayerActivity::class.java)
            playerIntent.putExtra("current_track", tracks[position])
            it.context.startActivity(playerIntent)
        }
    }

    override fun getItemCount() = tracks.size

    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }

}