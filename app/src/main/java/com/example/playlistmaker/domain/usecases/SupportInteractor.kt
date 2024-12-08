package com.example.playlistmaker.domain.usecases

import android.content.Context
import android.content.Intent
import android.net.Uri

class SupportInteractor(private val context: Context) {

    fun openTermsOfUse(url: String) {
        val termsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(termsIntent)
    }

    fun contactSupport(email: String, subject: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$email")
            putExtra(Intent.EXTRA_SUBJECT, subject)
        }
        context.startActivity(intent)
    }
}
