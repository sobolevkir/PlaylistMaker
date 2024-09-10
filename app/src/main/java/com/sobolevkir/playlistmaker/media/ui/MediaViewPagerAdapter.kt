package com.sobolevkir.playlistmaker.media.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sobolevkir.playlistmaker.favorites.ui.FavoritesFragment
import com.sobolevkir.playlistmaker.playlists.ui.PlaylistsFragment

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val tabs = arrayListOf(
        FavoritesFragment.newInstance(),
        PlaylistsFragment.newInstance()
    )

    override fun getItemCount(): Int = tabs.size

    override fun createFragment(position: Int): Fragment {
        return tabs[position]
    }

}
