package com.example.playlistmaker.presentation.media

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MediaPagerAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteFragment.newInstance()
            1 -> PlaylistFragment.newInstance()
            else -> throw IllegalStateException("Invalid position $position")
        }
    }
}
