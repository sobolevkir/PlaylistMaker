package com.sobolevkir.playlistmaker.tracklist

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.Utils

class TrackListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val trackName: TextView = itemView.findViewById(R.id.track_name)
    private val artistName: TextView = itemView.findViewById(R.id.artist_name)
    private val trackTime: TextView = itemView.findViewById(R.id.track_time)
    private val albumCover: ImageView = itemView.findViewById(R.id.album_cover)


    fun bind(track: Track) {
        trackName.text = track.trackName
        artistName.text = track.artistName
        trackTime.text = track.trackTime

        Glide.with(itemView)
            .load(track.artworkUrl100)
            .centerInside()
            .placeholder(R.drawable.placeholder)
            .transform(RoundedCorners(Utils.dpToPx(2f, itemView.context)))
            .into(albumCover)
    }
}