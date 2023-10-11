package com.sobolevkir.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val libraryButton = findViewById<Button>(R.id.btn_library)
        val searchButton = findViewById<Button>(R.id.btn_search)
        val settingsButton = findViewById<Button>(R.id.btn_settings)

        libraryButton.setOnClickListener {
            startActivity(Intent(this, LibraryActivity::class.java))
        }

        searchButton.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }

        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

    }

}