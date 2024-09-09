package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shareApp()
        contactSupport()
        checkTermsOfUse()
        binding.backButtonFromSettings.setOnClickListener { finish() }
    }

    private fun checkTermsOfUse() {
        binding.buttonTermsOfUse.setOnClickListener {
            val practicumOffer = getString(R.string.practicum_offer)
            val termsOfUseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(practicumOffer))
            startActivity(termsOfUseIntent)
        }
    }

    private fun contactSupport() {
        binding.buttonSupport.setOnClickListener {
            val email = getString(R.string.my_email)
            val message = getString(R.string.support_message)
            val body = getString(R.string.support_body)

            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
                putExtra(Intent.EXTRA_SUBJECT, message)
                putExtra(Intent.EXTRA_TEXT, body)
            }
            startActivity(supportIntent)
        }
    }

    private fun shareApp() {
        binding.buttonShareApp.setOnClickListener {
            val practicumUrl = getString(R.string.practicum_Url)
            val shareIntent = Intent().apply {
                type = "text/plain"
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, practicumUrl)
            }
            startActivity(shareIntent)
        }
    }
}

