package com.sobolevkir.playlistmaker.media.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.sobolevkir.playlistmaker.R
import com.sobolevkir.playlistmaker.common.util.viewBinding
import com.sobolevkir.playlistmaker.databinding.FragmentMediaBinding

class MediaFragment : Fragment(R.layout.fragment_media) {

    private val binding by viewBinding(FragmentMediaBinding::bind)
    private lateinit var tabMediator: TabLayoutMediator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)
        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.title_favorites)
                1 -> tab.text = getString(R.string.title_playlists)
            }
        }
        tabMediator.attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

}