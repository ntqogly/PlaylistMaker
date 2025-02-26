package com.example.playlistmaker.presentation.media

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.playlistmaker.presentation.media.favorite.FavoriteFragment
import com.example.playlistmaker.presentation.media.playlist.playlist.PlaylistFragment

class MediaPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteFragment.newInstance()
            1 -> PlaylistFragment.newInstance()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
