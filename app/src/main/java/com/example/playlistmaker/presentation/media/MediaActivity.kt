package com.example.playlistmaker.presentation.media

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivityMediaBinding
import com.google.android.material.tabs.TabLayoutMediator

class MediaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val adapter = MediaPagerAdapter(this)
//        binding.viewpager2.adapter = adapter

        TabLayoutMediator(binding.tabLayout, binding.viewpager2) { tab, position ->
            tab.text = when (position) {
                0 -> "Избранные треки"
                1 -> "Плейлисты"
                else -> throw IllegalStateException("Invalid position $position")
            }
        }.attach()

        binding.tbMedia.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}
