package com.sobolevkir.playlistmaker

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.sobolevkir.playlistmaker.tracklist.Track

class PlayerActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var albumCoverLarge: ImageView
    private lateinit var trackName: TextView
    private lateinit var artistName: TextView
    private lateinit var trackDuration: TextView
    private lateinit var collectionName: TextView
    private lateinit var collectionNameTitle: TextView
    private lateinit var releaseYear: TextView
    private lateinit var releaseYearTitle: TextView
    private lateinit var genre: TextView
    private lateinit var genreTitle: TextView
    private lateinit var country: TextView
    private lateinit var countryTitle: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)

        backButton = findViewById(R.id.btn_back)
        backButton.setOnClickListener { finish() }

        val track = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("current_track", Track::class.java)
        } else intent.getParcelableExtra("current_track")

        albumCoverLarge = findViewById(R.id.iv_album_cover_large)
        Glide.with(albumCoverLarge)
            .load(track?.getLargeCoverArtwork())
            .centerInside()
            .placeholder(R.drawable.cover_placeholder)
            .into(albumCoverLarge)

        trackName = findViewById(R.id.tv_track_name)
        trackName.text = track?.trackName

        artistName = findViewById(R.id.tv_artist_name)
        artistName.text = track?.artistName

        trackDuration = findViewById(R.id.tv_duration_value)
        trackDuration.text = track?.getFormattedTime()

        collectionName = findViewById(R.id.tv_collection_name_value)
        collectionNameTitle = findViewById(R.id.tv_collection_name_title)
        if ((track?.collectionName != null) && track.collectionName.isNotEmpty()) {
            collectionName.text = track.collectionName
        }
        else {
            collectionName.visibility = View.GONE
            collectionNameTitle.visibility = View.GONE
        }

        releaseYear = findViewById(R.id.tv_release_year_value)
        releaseYearTitle = findViewById(R.id.tv_release_year_title)
        if ((track?.releaseDate != null) && track.releaseDate.isNotEmpty()) {
            releaseYear.text = track.getReleaseYear()
        }
        else {
            releaseYear.visibility = View.GONE
            releaseYearTitle.visibility = View.GONE
        }

        genre = findViewById(R.id.tv_genre_value)
        genreTitle = findViewById(R.id.tv_genre_title)
        if ((track?.primaryGenreName != null) && track.primaryGenreName.isNotEmpty()) {
            genre.text = track.primaryGenreName
        }
        else {
            genre.visibility = View.GONE
            genreTitle.visibility = View.GONE
        }

        country = findViewById(R.id.tv_country_value)
        countryTitle = findViewById(R.id.tv_country_title)
        if ((track?.country != null) && track.country.isNotEmpty()) {
            country.text = track.country
        }
        else {
            country.visibility = View.GONE
            country.visibility = View.GONE
        }

    }
}