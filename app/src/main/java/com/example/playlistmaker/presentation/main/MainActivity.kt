package com.example.playlistmaker.presentation.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

//        binding.buttonSearch.setOnClickListener {
//            val searchIntent = Intent(this, SearchActivity::class.java)
//            startActivity(searchIntent)
//        }
//
//        binding.buttonMedia.setOnClickListener {
//            val mediaIntent = Intent(this, MediaActivity::class.java)
//            startActivity(mediaIntent)
//        }
//
//        binding.buttonSettings.setOnClickListener {
//            val settingsIntent = Intent(this, SettingsActivity::class.java)
//            startActivity(settingsIntent)
//        }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView: BottomNavigationView = binding.bottomNav
        bottomNavigationView.setupWithNavController(navController)

    }
}