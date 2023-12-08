package com.sobolevkir.playlistmaker.tracklist

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sobolevkir.playlistmaker.PlayerActivity
import com.sobolevkir.playlistmaker.databinding.LayoutTracklistItemBinding


class TrackListAdapter(
    private val tracks: MutableList<Track>,
    private val onTrackClickListener: OnTrackClickListener? = null
) : RecyclerView.Adapter<TrackListViewHolder>() {

    companion object {
        const val CURRENT_TRACK = "current_track"
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

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
            binding.root.setOnClickListener {
                if (clickDebounce()) {
                    onTrackClickListener?.onTrackClick(tracks[position])
                    val playerIntent = Intent(it.context, PlayerActivity::class.java)
                    playerIntent.putExtra(CURRENT_TRACK, tracks[position])
                    it.context.startActivity(playerIntent)
                }
            }
        }
    }

    override fun getItemCount() = tracks.size

    fun interface OnTrackClickListener {
        fun onTrackClick(track: Track)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

}