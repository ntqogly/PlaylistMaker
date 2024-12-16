package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.api.ISupportInteractor

class SupportRepository(private val context: Context) : ISupportInteractor {

    override fun openTermsOfUse(url: String) {
        val termsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(termsIntent)
    }

    override fun contactSupport(email: String, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
        }
        context.startActivity(intent)
    }
}
