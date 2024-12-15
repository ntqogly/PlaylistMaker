package com.example.playlistmaker.domain.usecases

import com.example.playlistmaker.data.repository.SupportRepository
import com.example.playlistmaker.domain.api.ISupportInteractor

class SupportInteractor(private val supportRepository: SupportRepository) : ISupportInteractor {

    override fun openTermsOfUse(url: String) {
        supportRepository.openTermsOfUse(url)
    }

    override fun contactSupport(email: String, subject: String, body: String) {
        supportRepository.contactSupport(email, subject, body)
    }
}
