package com.example.playlistmaker.presentation.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.player.PlayerActivity
import com.example.playlistmaker.presentation.adapter.TrackAdapter

//class SearchActivity : AppCompatActivity() {
//
//    private lateinit var binding: ActivitySearchBinding
//    private lateinit var trackAdapter: TrackAdapter
//    private lateinit var historyAdapter: TrackAdapter
//
//    private val searchTracksUseCase by lazy { Creator.provideSearchTrackUseCase() }
//    private val searchHistoryUseCase by lazy { Creator.provideSearchHistoryUseCase(this) }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivitySearchBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        binding.backButtonFromSearch.setOnClickListener { finish() }
//
//        setupRecyclerViews()
//        setupListeners()
//        setupSearchTextWatcher()
//        setupEditorActionListener()
//        loadSearchHistory()
//    }
//
//    private fun setupRecyclerViews() {
//        binding.rvTracks.layoutManager = LinearLayoutManager(this)
//        trackAdapter = TrackAdapter(mutableListOf()) { track ->
//            openPlayerActivity(track)
//        }
//        binding.rvTracks.adapter = trackAdapter
//
//        binding.rvHistoryTracks.layoutManager = LinearLayoutManager(this)
//        historyAdapter = TrackAdapter(mutableListOf()) { track ->
//            openPlayerActivity(track)
//        }
//        binding.rvHistoryTracks.adapter = historyAdapter
//    }
//
//    private fun setupListeners() {
//        binding.clearImageButton.setOnClickListener {
//            binding.etSearch.text.clear()
//            hideKeyboard()
//            loadSearchHistory()
//        }
//
//        binding.clearHistButton.setOnClickListener {
//            searchHistoryUseCase.clearHistory()
//            loadSearchHistory()
//        }
//    }
//
//    private fun setupSearchTextWatcher() {
//        binding.etSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s.isNullOrEmpty()) {
//                    loadSearchHistory()
//                } else {
//                    binding.clearImageButton.visibility = View.VISIBLE
//                }
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//    }
//
//    private fun setupEditorActionListener() {
//        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
//            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
//                hideKeyboard()
//                performSearch(binding.etSearch.text.toString())
//                true
//            } else {
//                false
//            }
//        }
//    }
//
//    private fun loadSearchHistory() {
//        val history = searchHistoryUseCase.getSearchHistory()
//        if (history.isNotEmpty()) {
//            historyAdapter.updateTracks(history)
//            historyAdapter.notifyDataSetChanged()
//            showHistoryView()
//        } else {
//            hideHistoryView()
//        }
//    }
//
//    private fun performSearch(query: String) {
//        binding.progressBarSearch.visibility = View.VISIBLE
//        searchTracksUseCase.executeAsync(query) { tracks: List<Track> ->
//            runOnUiThread {
//                binding.progressBarSearch.visibility = View.GONE
//                if (tracks.isNotEmpty()) {
//                    trackAdapter.updateTracks(tracks)
//                    trackAdapter.notifyDataSetChanged()
//                    showTrackList()
//                } else {
//                    showNothingFound()
//                }
//            }
//        }
//    }
//
//    private fun openPlayerActivity(track: Track) {
//        searchHistoryUseCase.addTrackToHistory(track)
//        val intent = Intent(this, PlayerActivity::class.java).apply {
//            putExtra("TRACK_EXTRA", track)
//        }
//        startActivity(intent)
//    }
//
//    private fun showTrackList() {
//        with(binding) {
//            rvTracks.visibility = View.VISIBLE
//            linearLayoutSearch.visibility = View.GONE
//            linearLayoutInternet.visibility = View.GONE
//            linearLayoutHistory.visibility = View.GONE
//        }
//    }
//
//    private fun showHistoryView() {
//        with(binding) {
//            linearLayoutHistory.visibility = View.VISIBLE
//            rvHistoryTracks.visibility = View.VISIBLE
//            rvTracks.visibility = View.GONE
//            linearLayoutSearch.visibility = View.GONE
//            linearLayoutInternet.visibility = View.GONE
//        }
//    }
//
//    private fun hideHistoryView() {
//        with(binding) {
//            linearLayoutHistory.visibility = View.GONE
//            rvHistoryTracks.visibility = View.GONE
//            rvTracks.visibility = View.GONE
//            linearLayoutSearch.visibility = View.GONE
//            linearLayoutInternet.visibility = View.GONE
//        }
//    }
//
//    private fun showNothingFound() {
//        with(binding) {
//            linearLayoutSearch.visibility = View.VISIBLE
//            rvTracks.visibility = View.GONE
//            linearLayoutInternet.visibility = View.GONE
//            linearLayoutHistory.visibility = View.GONE
//        }
//    }
//
//    private fun hideKeyboard() {
//        currentFocus?.let { view ->
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }
//}

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val viewModel: SearchViewModel by viewModels {
        Creator.provideSearchViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButtonFromSearch.setOnClickListener { finish() }

        setupRecyclerViews()
        setupListeners()
        setupSearchTextWatcher()
        setupEditorActionListener()
        loadSearchHistory()

        observeViewModel()
    }

    private fun setupRecyclerViews() {
        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        trackAdapter = TrackAdapter(mutableListOf()) { track ->
            openPlayerActivity(track)
        }
        binding.rvTracks.adapter = trackAdapter

        binding.rvHistoryTracks.layoutManager = LinearLayoutManager(this)
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
        }

        binding.clearHistButton.setOnClickListener {
            Creator.provideSearchHistoryUseCase(this).clearHistory()
            loadSearchHistory()
        }
    }

    private fun setupSearchTextWatcher() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    loadSearchHistory()
                } else {
                    binding.clearImageButton.visibility = View.VISIBLE
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setupEditorActionListener() {
        binding.etSearch.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                hideKeyboard()
                viewModel.searchTracks(binding.etSearch.text.toString())
                true
            } else {
                false
            }
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(this) { state ->
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
        val history = Creator.provideSearchHistoryUseCase(this).getSearchHistory()
        if (history.isNotEmpty()) {
            historyAdapter.updateTracks(history)
            historyAdapter.notifyDataSetChanged()
            showHistoryView()
        } else {
            hideHistoryView()
        }
    }

    private fun openPlayerActivity(track: Track) {
        Creator.provideSearchHistoryUseCase(this).addTrackToHistory(track)
        val intent = Intent(this, PlayerActivity::class.java).apply {
            putExtra("TRACK_EXTRA", track)
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

    private fun hideKeyboard() {
        currentFocus?.let { view ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}