package com.example.playlistmaker.presentation.search

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.domain.usecases.SearchHistoryUseCase
import com.example.playlistmaker.presentation.player.PlayerActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class FragmentSearch : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val viewModel by viewModel<SearchViewModel>()
    private val searchHistoryUseCase: SearchHistoryUseCase by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupListeners()
        setupSearchTextWatcher()
        setupEditorActionListener()
        loadSearchHistory()
        observeViewModel()
        setThemeSpecificImage(binding.ivNothingFound)

        bottomNavHide()

    }

    private fun bottomNavHide() {
        val bottomNavigationView =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_nav)

        val contentView = requireActivity().findViewById<FrameLayout>(android.R.id.content)
        contentView.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            contentView.getWindowVisibleDisplayFrame(r)
            val screenHeight = contentView.rootView.height
            val keypadHeight = screenHeight - r.bottom

            if (keypadHeight > screenHeight * 0.15) {
                bottomNavigationView.visibility = View.GONE
            } else {
                bottomNavigationView.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRecyclerViews() {
        binding.rvTracks.layoutManager = LinearLayoutManager(requireContext())
        trackAdapter = TrackAdapter(mutableListOf()) { track ->
            openPlayerActivity(track)
        }
        binding.rvTracks.adapter = trackAdapter

        binding.rvHistoryTracks.layoutManager = LinearLayoutManager(requireContext())
        historyAdapter = TrackAdapter(mutableListOf()) { track ->
            openPlayerActivity(track)
        }
        binding.rvHistoryTracks.adapter = historyAdapter
    }

    private fun setupListeners() {
        binding.clearImageButton.setOnClickListener {
            binding.etSearch.text.clear()
            hideKeyboard()
            viewModel.clearSearch()
            updateClearButtonVisibility("")
        }

        binding.clearHistButton.setOnClickListener {
            searchHistoryUseCase.clearHistory()
            loadSearchHistory()
        }

        binding.refreshButton.setOnClickListener {
            if (viewModel.checkInternetConnection()) {
                viewModel.searchTracks(binding.etSearch.text.toString())
            } else {
                showNoInternet()
            }
        }


    }

    private fun updateClearButtonVisibility(query: CharSequence?) {
        binding.clearImageButton.visibility = if (query.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    private fun setupSearchTextWatcher() {
        val handler = Handler(requireContext().mainLooper)
        var searchRunnable: Runnable? = null

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    loadSearchHistory()
                    binding.clearImageButton.visibility = View.INVISIBLE
                    binding.linearLayoutSearch.visibility = View.GONE

                    searchRunnable?.let { handler.removeCallbacks(it) }
                } else {
                    binding.clearImageButton.visibility = View.VISIBLE
                    binding.linearLayoutHistory.visibility = View.GONE

                    searchRunnable?.let { handler.removeCallbacks(it) }
                    searchRunnable = Runnable {
                        if (s.isNotEmpty()) {
                            viewModel.searchTracks(s.toString())
                        }
                    }
                    handler.postDelayed(searchRunnable!!, 2000)

                }

            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupEditorActionListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                if (viewModel.checkInternetConnection()) {
                    viewModel.searchTracks(binding.etSearch.text.toString())
                } else {
                    showNoInternet()
                }
                true
            } else {
                false
            }
        }

    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is SearchViewModel.SearchState.Initial -> loadSearchHistory()
                is SearchViewModel.SearchState.Loading -> binding.progressBarSearch.visibility =
                    View.VISIBLE

                is SearchViewModel.SearchState.Success -> {
                    binding.progressBarSearch.visibility = View.GONE
                    trackAdapter.updateTracks(state.tracks)
                    showTrackList()
                }

                is SearchViewModel.SearchState.Empty -> {
                    binding.progressBarSearch.visibility = View.GONE
                    showNothingFound()
                }
            }
        }
    }

    private fun loadSearchHistory() {
        val history = searchHistoryUseCase.getSearchHistory()
        if (history.isNotEmpty()) {
            historyAdapter.updateTracks(history)
            historyAdapter.notifyDataSetChanged()
            showHistoryView()
        } else {
            hideHistoryView()
        }
    }

    private fun openPlayerActivity(track: Track) {
        searchHistoryUseCase.addTrackToHistory(track)
        val intent = Intent(requireContext(), PlayerActivity::class.java).apply {
            putExtra("TRACK_EXTRA", Gson().toJson(track))
        }
        startActivity(intent)
    }

    private fun showTrackList() {
        with(binding) {
            rvTracks.visibility = View.VISIBLE
            linearLayoutSearch.visibility = View.GONE
            linearLayoutInternet.visibility = View.GONE
            linearLayoutHistory.visibility = View.GONE
        }
    }

    private fun showHistoryView() {
        with(binding) {
            linearLayoutHistory.visibility = View.VISIBLE
            rvHistoryTracks.visibility = View.VISIBLE
            rvTracks.visibility = View.GONE
            linearLayoutSearch.visibility = View.GONE
            linearLayoutInternet.visibility = View.GONE
        }
    }

    private fun hideHistoryView() {
        with(binding) {
            linearLayoutHistory.visibility = View.GONE
            rvHistoryTracks.visibility = View.GONE
            rvTracks.visibility = View.GONE
            linearLayoutSearch.visibility = View.GONE
            linearLayoutInternet.visibility = View.GONE
        }
    }

    private fun showNothingFound() {
        with(binding) {
            linearLayoutSearch.visibility = View.VISIBLE
            rvTracks.visibility = View.GONE
            linearLayoutInternet.visibility = View.GONE
            linearLayoutHistory.visibility = View.GONE
        }
    }

    private fun showNoInternet() {
        with(binding) {
            linearLayoutInternet.visibility = View.VISIBLE
            rvTracks.visibility = View.GONE
            linearLayoutSearch.visibility = View.GONE
            linearLayoutHistory.visibility = View.GONE
        }
    }

    private fun hideKeyboard() {
        val view = requireActivity().currentFocus
        if (view != null) {
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }


    private fun setThemeSpecificImage(imageView: ImageView) {
        val isNightMode =
            (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

        if (isNightMode) {
            imageView.setImageResource(R.drawable.img_search_dark)
        } else {
            imageView.setImageResource(R.drawable.img_search_light)
        }
    }

    companion object {

    }
}