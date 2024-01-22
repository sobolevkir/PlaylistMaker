package com.sobolevkir.playlistmaker.presentation.ui.player

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.databinding.ActivityPlayerBinding
import com.sobolevkir.playlistmaker.domain.api.PlayerInteractor
import com.sobolevkir.playlistmaker.domain.models.Track
import com.sobolevkir.playlistmaker.presentation.ui.search.SearchActivity.Companion.CURRENT_TRACK
import com.sobolevkir.playlistmaker.util.Creator

class PlayerActivity : AppCompatActivity() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private lateinit var updateCurrentPosition: Runnable

    private lateinit var playerInteractor: PlayerInteractor

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val localStorage = Creator.provideLocalStorage()
        val track = Gson().fromJson(localStorage.read(CURRENT_TRACK, null), Track::class.java)

        track?.let {
            if (it.previewUrl.isNotEmpty()) {
                playerInteractor = Creator.providePlayerInteractor(it.previewUrl)
                playerInteractor.preparePlayer(
                    preparedConsumer = {
                        binding.btnPlayControl.run {
                            alpha = 1.0f
                            isEnabled = true
                        }
                    },
                    completionConsumer = {
                        mainThreadHandler.removeCallbacks(updateCurrentPosition)
                        binding.tvCurrentPosition.text =
                            resources.getString(R.string.hint_track_duration)
                        binding.btnPlayControl.setImageResource(R.drawable.btn_play_icon)
                    })
                with(binding.btnPlayControl) {
                    setOnClickListener {
                        playerInteractor.playbackControl(
                            startingConsumer = {
                                binding.btnPlayControl.setImageResource(R.drawable.btn_pause_icon)
                                mainThreadHandler.postDelayed(updateCurrentPosition, UPDATER_DELAY)
                            },
                            pausingConsumer = {
                                binding.btnPlayControl.setImageResource(R.drawable.btn_play_icon)
                                mainThreadHandler.removeCallbacks(updateCurrentPosition)
                            })
                    }
                }
                updateCurrentPosition = object : Runnable {
                    override fun run() {
                        binding.tvCurrentPosition.text = playerInteractor.getCurrentPosition()
                        mainThreadHandler.postDelayed(this, UPDATER_DELAY)
                    }
                }
            }

            Glide.with(binding.root.context).load(it.artworkUrl512).centerInside()
                .placeholder(R.drawable.cover_placeholder).into(binding.ivAlbumCoverLarge)

            binding.tvTrackName.text = it.trackName
            binding.tvArtistName.text = it.artistName

            if (it.formattedTrackTime.isNotEmpty()) {
                binding.tvDurationValue.text = it.formattedTrackTime
            } else {
                binding.tvDurationValue.visibility = View.GONE
                binding.tvDurationTitle.visibility = View.GONE
            }

            if (it.collectionName.isNotEmpty()) {
                binding.tvCollectionNameValue.text = it.collectionName
            } else {
                binding.tvCollectionNameValue.visibility = View.GONE
                binding.tvCollectionNameTitle.visibility = View.GONE
            }

            if (it.releaseYear.isNotEmpty()) {
                binding.tvReleaseYearValue.text = it.releaseYear
            } else {
                binding.tvReleaseYearValue.visibility = View.GONE
                binding.tvReleaseYearTitle.visibility = View.GONE
            }

            if (it.primaryGenreName.isNotEmpty()) {
                binding.tvGenreValue.text = it.primaryGenreName
            } else {
                binding.tvGenreValue.visibility = View.GONE
                binding.tvGenreTitle.visibility = View.GONE
            }

            if (it.country.isNotEmpty()) {
                binding.tvCountryValue.text = it.country
            } else {
                binding.tvCountryValue.visibility = View.GONE
                binding.tvCountryTitle.visibility = View.GONE
            }
        }
    }

    override fun onPause() {
        super.onPause()
        if (playerInteractor.isPlaying()) {
            playerInteractor.pausePlayer()
            binding.btnPlayControl.setImageResource(R.drawable.btn_play_icon)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(updateCurrentPosition)
        playerInteractor.releasePlayer()
    }

    companion object {
        private const val UPDATER_DELAY = 300L
    }

    /*private fun preparePlayer(url: String?) {
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
   }*/


}