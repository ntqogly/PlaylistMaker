package com.example.playlistmaker.data.repository

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.domain.api.ISupportInteractor

class SupportRepository(private val context: Context) : ISupportInteractor {

    override fun openTermsOfUse(url: String) {
        val termsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(termsIntent)

    }

    override fun contactSupport(email: String, subject: String, body: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(intent)
    }
}
