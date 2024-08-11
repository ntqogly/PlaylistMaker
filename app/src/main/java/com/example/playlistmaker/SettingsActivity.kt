package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        shareApp()
        contactSupport()
        checkTermsOfUse()
        val backButton = findViewById<Button>(R.id.back_button_from_settings).setOnClickListener { finish() }
    }

    private fun checkTermsOfUse() {
        val termsOfUseButton = findViewById<TextView>(R.id.button_terms_of_use)
        termsOfUseButton.setOnClickListener {
            val practicumOffer = getString(R.string.practicum_offer)
            val termsOfUseIntent = Intent(Intent.ACTION_VIEW, Uri.parse(practicumOffer))
            startActivity(termsOfUseIntent)
        }
    }

    private fun contactSupport() {
        val supportButton = findViewById<TextView>(R.id.button_support)
        supportButton.setOnClickListener {
            val email = getString(R.string.my_email)
            val message = getString(R.string.support_message)
            val body = getString(R.string.support_body)

            val supportIntent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(email) )
                putExtra(Intent.EXTRA_SUBJECT, message)
                putExtra(Intent.EXTRA_TEXT, body)
            }
            startActivity(supportIntent)
        }
    }

    private fun shareApp() {
        val shareButton = findViewById<TextView>(R.id.button_share_app)
        shareButton.setOnClickListener {
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

