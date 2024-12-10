package com.example.playlistmaker.domain.api

interface ISupportInteractor {
    fun openTermsOfUse(url: String)
    fun contactSupport(email: String, subject: String, body: String)
}