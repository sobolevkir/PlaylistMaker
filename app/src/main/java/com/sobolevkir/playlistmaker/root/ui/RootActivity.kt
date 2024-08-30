package com.sobolevkir.playlistmaker.root.ui

import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding
    private var previousDestinationId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.createPlaylistFragment, R.id.playerFragment -> animateBottomNavigationView(false)
                else -> {
                    if (previousDestinationId == R.id.createPlaylistFragment ||
                        previousDestinationId == R.id.playerFragment
                    ) {
                        animateBottomNavigationView(true)
                    }
                }
            }
            previousDestinationId = destination.id
        }

    }

    private fun animateBottomNavigationView(show: Boolean) {
        val animation = if (show) {
            AnimationUtils.loadAnimation(this, R.anim.bottom_nav_show)
        } else {
            AnimationUtils.loadAnimation(this, R.anim.bottom_nav_hide)
        }
        binding.bottomNavigationView.startAnimation(animation)
        binding.bottomNavigationView.isVisible = show
    }

}