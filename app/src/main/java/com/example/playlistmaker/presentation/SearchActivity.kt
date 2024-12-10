package com.example.playlistmaker.presentation

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.Creator
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.models.Track

//class SearchActivity : AppCompatActivity() {
//
//    private var restoredText = ""
//    private val trackList = ArrayList<Track>()
//    private lateinit var binding: ActivitySearchBinding
//    private lateinit var trackAdapter: TrackAdapter
//    private lateinit var searchHistory: SearchHistory
//    private var mainThreadHandler: Handler? = null
//    private var searchRunnable: Runnable? = null
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        binding = ActivitySearchBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }
//        mainThreadHandler = Handler(Looper.getMainLooper())
//
//        val sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE)
//        searchHistory = SearchHistory(sharedPreferences)
//
//        trackAdapter = TrackAdapter(trackList, searchHistory) { track ->
//            searchHistory.addTrack(track)
//        }
//
//        setupTracksRecyclerView()
//        setupHistoryRecyclerView()
//        setupListeners()
//        loadSearchHistory()
//
//        binding.backButtonFromSearch.setOnClickListener { finish() }
//        binding.clearImageButton.setOnClickListener {
//            binding.etSearch.setText("")
//            hideKeyboard()
//            trackList.clear()
//            trackAdapter.notifyDataSetChanged()
//            loadSearchHistory()
//        }
//        changeListener()
//        refreshButton()
//        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
//            if (actionId == EditorInfo.IME_ACTION_DONE) {
//                loadTrack()
//                hideKeyboard()
//                true
//            } else {
//                false
//            }
//        }
//    }
//
//    private fun loadSearchHistory() {
//        val history = searchHistory.getHistory()
//        if (history.isNotEmpty()) {
//            binding.linearLayoutHistory.visibility = View.VISIBLE
//            binding.rvHistoryTracks.visibility = View.VISIBLE
//            trackAdapter.updateTracks(history)
//        } else {
//            binding.linearLayoutHistory.visibility = View.GONE
//            binding.rvHistoryTracks.visibility = View.GONE
//        }
//        binding.rvHistoryTracks.adapter?.notifyDataSetChanged()
//        binding.rvTracks.visibility = View.GONE
//    }
//
//    private fun setupListeners() {
//        binding.clearHistButton.setOnClickListener {
//            searchHistory.clearHistory()
//            loadSearchHistory()
//        }
//    }
//
//    private fun setupTracksRecyclerView() {
//        binding.rvTracks.layoutManager = LinearLayoutManager(this)
//        binding.rvTracks.adapter = trackAdapter
//    }
//
//    private fun setupHistoryRecyclerView() {
//        binding.rvHistoryTracks.layoutManager = LinearLayoutManager(this)
//        binding.rvHistoryTracks.adapter = trackAdapter
//    }
//
//
//    private fun changeListener() {
//        binding.etSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//                if (s.isNullOrEmpty()) {
//                    loadSearchHistory()
//                    binding.clearImageButton.visibility = View.INVISIBLE
//                    binding.linearLayoutSearch.visibility = View.GONE
//                } else {
//                    binding.clearImageButton.visibility = View.VISIBLE
//                    binding.linearLayoutHistory.visibility = View.GONE
//
//                    searchRunnable?.let { mainThreadHandler?.removeCallbacks(it) }
//                    searchRunnable = Runnable { loadTrack() }
//                    mainThreadHandler?.postDelayed(searchRunnable!!, 2000)
//                }
//                restoredText = binding.etSearch.text.toString()
//            }
//
//            override fun afterTextChanged(s: Editable?) {}
//        })
//    }
//
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString("etText", restoredText)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        restoredText = savedInstanceState.getString("etText").toString()
//    }
//
//    private fun hideKeyboard() {
//        currentFocus?.let { view ->
//            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
//            imm.hideSoftInputFromWindow(view.windowToken, 0)
//        }
//    }
//
//    private fun loadTrack() {
//        binding.progressBarSearch.visibility = View.VISIBLE
//        ApiFactory.apiService.search(binding.etSearch.text.toString())
//            .enqueue(object : Callback<TrackResponse> {
//                override fun onResponse(
//                    call: Call<TrackResponse>, response: Response<TrackResponse>
//                ) {
//                    binding.progressBarSearch.visibility = View.GONE
//                    if (response.isSuccessful) {
//                        val tracks = response.body()?.results ?: emptyList()
//                        if (tracks.isNotEmpty()) {
//                            trackAdapter.updateTracks(tracks)
//                            trackAdapter.notifyDataSetChanged()
//                            showTrackList()
//                        } else {
//                            nothingFound()
//                        }
//                    } else {
//                        noInternet()
//                    }
//                }
//
//                override fun onFailure(p0: Call<TrackResponse>, p1: Throwable) {
//                    noInternet()
//                }
//            })
//    }
//
//    private fun showTrackList() {
//        with(binding) {
//            linearLayoutSearch.visibility = View.GONE
//            linearLayoutInternet.visibility = View.GONE
//            binding.linearLayoutHistory.visibility = View.GONE
//            rvTracks.visibility = View.VISIBLE
//        }
//    }
//
//    private fun noInternet() {
//        with(binding) {
//            linearLayoutSearch.visibility = View.GONE
//            linearLayoutInternet.visibility = View.VISIBLE
//            binding.linearLayoutHistory.visibility = View.GONE
//            rvTracks.visibility = View.GONE
//        }
//    }
//
//    private fun nothingFound() {
//        with(binding) {
//            linearLayoutSearch.visibility = View.VISIBLE
//            linearLayoutInternet.visibility = View.GONE
//            binding.linearLayoutHistory.visibility = View.GONE
//            rvTracks.visibility = View.GONE
//        }
//    }
//
//    private fun refreshButton() {
//        binding.refreshButton.setOnClickListener {
//            loadTrack()
//        }
//    }
//}

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var historyAdapter: TrackAdapter

    private val searchTracksUseCase by lazy { Creator.provideSearchTrackUseCase() }
    private val searchHistoryUseCase by lazy { Creator.provideSearchHistoryUseCase(this) }

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
            loadSearchHistory()
        }

        binding.clearHistButton.setOnClickListener {
            searchHistoryUseCase.clearHistory()
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
                performSearch(binding.etSearch.text.toString())
                true
            } else {
                false
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

    private fun performSearch(query: String) {
        binding.progressBarSearch.visibility = View.VISIBLE
        searchTracksUseCase.executeAsync(query) { tracks: List<Track> ->
            runOnUiThread {
                binding.progressBarSearch.visibility = View.GONE
                if (tracks.isNotEmpty()) {
                    trackAdapter.updateTracks(tracks)
                    trackAdapter.notifyDataSetChanged()
                    showTrackList()
                } else {
                    showNothingFound()
                }
            }
        }
    }

    private fun openPlayerActivity(track: Track) {
        searchHistoryUseCase.addTrackToHistory(track)
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
