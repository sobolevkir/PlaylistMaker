package com.sobolevkir.playlistmaker

import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sobolevkir.playlistmaker.SearchActivity.Companion.CURRENT_TRACK
import com.sobolevkir.playlistmaker.databinding.ActivityPlayerBinding
import com.sobolevkir.playlistmaker.tracklist.Track
import java.text.SimpleDateFormat
import java.util.Locale

class PlayerActivity : AppCompatActivity() {

    companion object {
        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3
        private const val CURRENT_POSITION_DELAY = 300L
    }

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private lateinit var updateCurrentPosition: Runnable

    private var playerState = STATE_DEFAULT
    private var mediaPlayer = MediaPlayer()

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CURRENT_TRACK, Track::class.java)
        } else intent.getParcelableExtra<Track>(CURRENT_TRACK)

        if (!track?.previewUrl.isNullOrEmpty()) {
            preparePlayer(track?.previewUrl)
            binding.btnPlayControl.setOnClickListener {
                playbackControl()
            }
            updateCurrentPosition = object : Runnable {
                override fun run() {
                    binding.tvCurrentPosition.text =
                        SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(mediaPlayer.currentPosition)
                    mainThreadHandler.postDelayed(this, CURRENT_POSITION_DELAY)
                }
            }
        }

        Glide.with(binding.root.context)
            .load(track?.getLargeCoverArtwork())
            .centerInside()
            .placeholder(R.drawable.cover_placeholder)
            .into(binding.ivAlbumCoverLarge)

        binding.tvTrackName.text = track?.trackName
        binding.tvArtistName.text = track?.artistName
        binding.tvDurationValue.text = track?.getFormattedTime()

        if (!track?.collectionName.isNullOrEmpty()) {
            binding.tvCollectionNameValue.text = track?.collectionName
        } else {
            binding.tvCollectionNameValue.visibility = View.GONE
            binding.tvCollectionNameValue.visibility = View.GONE
        }

        if (!track?.releaseDate.isNullOrEmpty()) {
            binding.tvReleaseYearValue.text = track?.getReleaseYear()
        } else {
            binding.tvReleaseYearValue.visibility = View.GONE
            binding.tvReleaseYearTitle.visibility = View.GONE
        }

        if (!track?.primaryGenreName.isNullOrEmpty()) {
            binding.tvGenreValue.text = track?.primaryGenreName
        } else {
            binding.tvGenreValue.visibility = View.GONE
            binding.tvGenreTitle.visibility = View.GONE
        }

        if (!track?.country.isNullOrEmpty()) {
            binding.tvCountryValue.text = track?.country
        } else {
            binding.tvCountryValue.visibility = View.GONE
            binding.tvCountryTitle.visibility = View.GONE
        }

    }

    override fun onPause() {
        super.onPause()
        if (mediaPlayer.isPlaying) pausePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(updateCurrentPosition)
        mediaPlayer.release()
    }

    private fun playbackControl() {
        when (playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }

            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun preparePlayer(url: String?) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            with(binding.btnPlayControl) {
                isEnabled = true
                alpha = 1.0f
            }
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            binding.tvCurrentPosition.text = resources.getString(R.string.hint_track_duration)
            binding.btnPlayControl.setImageResource(R.drawable.btn_play_icon)
            mainThreadHandler.removeCallbacks(updateCurrentPosition)
            playerState = STATE_PREPARED
        }
    }

    private fun startPlayer() {
        binding.btnPlayControl.setImageResource(R.drawable.btn_pause_icon)
        mediaPlayer.start()
        playerState = STATE_PLAYING
        mainThreadHandler.postDelayed(updateCurrentPosition, CURRENT_POSITION_DELAY)
    }

    private fun pausePlayer() {
        binding.btnPlayControl.setImageResource(R.drawable.btn_play_icon)
        mainThreadHandler.removeCallbacks(updateCurrentPosition)
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }


}