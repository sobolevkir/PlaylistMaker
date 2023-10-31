package com.sobolevkir.playlistmaker

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.sobolevkir.playlistmaker.databinding.ActivityPlayerBinding
import com.sobolevkir.playlistmaker.tracklist.Track

class PlayerActivity : AppCompatActivity() {

    companion object{
        const val CURRENT_TRACK = "current_track"
    }

    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBack.setOnClickListener { finish() }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(CURRENT_TRACK, Track::class.java)
        } else intent.getParcelableExtra(CURRENT_TRACK)


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
}