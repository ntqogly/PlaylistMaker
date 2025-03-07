package com.example.playlistmaker.presentation.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSettingsBinding
import com.example.playlistmaker.domain.api.ISupportInteractor
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSettings : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<SettingsViewModel>()
    private val supportInteractor: ISupportInteractor by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        setupThemeSwitch()
        setupSupportButtons()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.switchTheme.isChecked = state.isDarkMode
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
            supportInteractor.contactSupport(email, subject, body)
        }

        binding.buttonTermsOfUse.setOnClickListener {
            val termsUrl = getString(R.string.practicum_offer)
            supportInteractor.openTermsOfUse(termsUrl)
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