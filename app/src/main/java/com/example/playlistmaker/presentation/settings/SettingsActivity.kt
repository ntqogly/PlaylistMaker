package com.example.playlistmaker.presentation.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels {
        Creator.provideSettingsViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        observeViewModel()
        setupThemeSwitch()
        setupSupportButtons()
    }

    private fun setupToolbar() {
        binding.backButtonFromSettings.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.isDarkMode.observe(this) { isDarkMode ->
            binding.switchTheme.isChecked = isDarkMode
        }
    }

    private fun setupThemeSwitch() {
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleTheme(isChecked)
        }
    }

    private fun setupSupportButtons() {
        binding.buttonShareApp.setOnClickListener {
            shareApp()
        }

        binding.buttonSupport.setOnClickListener {
            val email = getString(R.string.my_email)
            val subject = getString(R.string.support_subject)
            val body = getString(R.string.support_body)
            Creator.provideSupportInteractor(this).contactSupport(email, subject, body)
        }

        binding.buttonTermsOfUse.setOnClickListener {
            val termsUrl = getString(R.string.practicum_offer)
            Creator.provideSupportInteractor(this).openTermsOfUse(termsUrl)
        }
    }

    private fun shareApp() {
        val shareIntent = android.content.Intent().apply {
            action = android.content.Intent.ACTION_SEND
            putExtra(
                android.content.Intent.EXTRA_TEXT, getString(R.string.practicum_Url)
            )
            type = "text/plain"
        }
        startActivity(android.content.Intent.createChooser(shareIntent, null))
    }
}