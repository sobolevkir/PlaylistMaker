package com.sobolevkir.playlistmaker.root.ui

import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isInHiddenDestinations =
                destination.id == R.id.createPlaylistFragment || destination.id == R.id.playerFragment
            if (binding.bottomNavigationView.isVisible && isInHiddenDestinations) {
                animateBottomNavigationView(false)
            } else if (!binding.bottomNavigationView.isVisible && !isInHiddenDestinations) {
                animateBottomNavigationView(true)
            }
        }

    }

    private fun animateBottomNavigationView(show: Boolean) {
        val animation = AnimationUtils.loadAnimation(
            this,
            if (show) R.anim.bottom_nav_show else R.anim.bottom_nav_hide
        )
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {
                if (show) binding.bottomNavigationView.isVisible = true
            }

            override fun onAnimationEnd(animation: Animation?) {
                if (!show) binding.bottomNavigationView.isVisible = false
            }

            override fun onAnimationRepeat(animation: Animation?) {}
        })
        binding.bottomNavigationView.startAnimation(animation)
    }

}