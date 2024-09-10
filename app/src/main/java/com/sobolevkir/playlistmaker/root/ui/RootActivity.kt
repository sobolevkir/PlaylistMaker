package com.sobolevkir.playlistmaker.root.ui

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnLayout
import androidx.core.view.doOnNextLayout
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
            val isInHiddenDestinations = destination.id in listOf(
                R.id.playlistCreateFragment, R.id.playerFragment, R.id.playlistInfoFragment
            )
            binding.rootFragmentContainerView.doOnNextLayout {
                setBottomNavigationViewVisibility(!isInHiddenDestinations)
            }
        }
    }

    private fun setBottomNavigationViewVisibility(isVisible: Boolean) {
        val animRes = if (isVisible) R.anim.bottom_nav_show else R.anim.bottom_nav_hide
        binding.bottomNavigationView.apply {
            startAnimation(AnimationUtils.loadAnimation(this@RootActivity, animRes))
            visibility = if (isVisible) View.VISIBLE else View.GONE
        }
    }
}