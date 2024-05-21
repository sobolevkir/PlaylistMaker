package com.sobolevkir.playlistmaker.player.ui

import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.domain.model.Track
import com.sobolevkir.playlistmaker.databinding.ActivityPlayerBinding
import com.sobolevkir.playlistmaker.player.domain.model.PlayerState
import com.sobolevkir.playlistmaker.player.presentation.PlayerViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class PlayerActivity : AppCompatActivity() {

    private val args: PlayerActivityArgs by navArgs()
    private val viewModel: PlayerViewModel by viewModel {
        parametersOf(args.currentTrackInfo)
    }
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        lifecycle.addObserver(viewModel)
        viewModel.getCurrentTrackLiveData().observe(this) { setTrackInfo(it) }
        viewModel.getPlayerStateLiveData().observe(this) { playerState ->
            changePlayButtonStyle(playerState)
        }
        viewModel.getProgressLiveData().observe(this) { progress ->
            binding.tvCurrentPosition.text = progress
        }
        initListeners()
    }

    private fun initListeners() {
        with(binding) {
            binding.toolbar.setNavigationOnClickListener { finish() }
            btnPlayControl.setOnClickListener { viewModel.playbackControl() }
            btnFavorite.setOnClickListener { viewModel.onFavoriteButtonClick() }
        }
    }

    private fun setTrackInfo(track: Track) {
        with(binding) {
            btnFavorite.setImageDrawable(getFavoriteButtonDrawable(track.isFavorite))
            Glide.with(this@PlayerActivity).load(track.artworkUrl512).centerInside()
                .placeholder(R.drawable.cover_placeholder).into(ivAlbumCoverLarge)
            tvTrackName.text = track.trackName
            tvArtistName.text = track.artistName
            if (track.formattedTrackTime.isNotEmpty()) {
                tvDurationValue.text = track.formattedTrackTime
            } else {
                tvDurationValue.isVisible = false
                tvDurationTitle.isVisible = false
            }
            if (track.collectionName.isNotEmpty()) {
                tvCollectionNameValue.text = track.collectionName
            } else {
                tvCollectionNameValue.isVisible = false
                tvCollectionNameTitle.isVisible = false
            }
            if (track.releaseYear.isNotEmpty()) {
                tvReleaseYearValue.text = track.releaseYear
            } else {
                tvReleaseYearValue.isVisible = false
                tvReleaseYearTitle.isVisible = false
            }
            if (track.primaryGenreName.isNotEmpty()) {
                tvGenreValue.text = track.primaryGenreName
            } else {
                tvGenreValue.isVisible = false
                tvGenreTitle.isVisible = false
            }
            if (track.country.isNotEmpty()) {
                tvCountryValue.text = track.country
            } else {
                tvCountryValue.isVisible = false
                tvCountryTitle.isVisible = false
            }
        }
    }

    private fun changePlayButtonStyle(playerState: PlayerState) {
        binding.btnPlayControl.run {
            when (playerState) {
                PlayerState.PREPARED -> {
                    this.alpha = 1.0f
                    this.isEnabled = true
                    this.setImageResource(R.drawable.btn_play_icon)
                }

                PlayerState.PLAYING -> this.setImageResource(R.drawable.btn_pause_icon)
                PlayerState.PAUSED -> this.setImageResource(R.drawable.btn_play_icon)
                else -> {
                    this.alpha = 0.25f
                    this.isEnabled = false
                }
            }
        }
    }

    private fun getFavoriteButtonDrawable(inFavorite: Boolean): Drawable? {
        return ContextCompat.getDrawable(
            this@PlayerActivity,
            if (inFavorite) R.drawable.btn_favorite_active_icon else R.drawable.btn_favorite_inactive_icon
        )
    }

}