package com.example.playlistmaker

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.models.Track
import com.example.playlistmaker.models.TrackResponse
import com.example.playlistmaker.network.ApiFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    private var restoredText = ""
    private val trackList = ArrayList<Track>()
    private lateinit var binding: ActivitySearchBinding
    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistory: SearchHistory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val sharedPreferences = getSharedPreferences("preferences", MODE_PRIVATE)
        searchHistory = SearchHistory(sharedPreferences)

        trackAdapter = TrackAdapter(trackList, searchHistory) { track ->
            searchHistory.addTrack(track)
//            loadSearchHistory()
        }

        setupTracksRecyclerView()
        setupHistoryRecyclerView()
        setupListeners()
        loadSearchHistory()

        binding.backButtonFromSearch.setOnClickListener { finish() }
        binding.clearImageButton.setOnClickListener {
            binding.etSearch.setText("")
            hideKeyboard()
            trackList.clear()
            trackAdapter.notifyDataSetChanged()
            loadSearchHistory()
        }
        changeListener()
        refreshButton()
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadTrack()
                hideKeyboard()
                true
            } else {
                false
            }
        }
    }

    private fun loadSearchHistory() {
        val history = searchHistory.getHistory()
        if (history.isNotEmpty()) {
            binding.linearLayoutHistory.visibility = View.VISIBLE
            binding.rvHistoryTracks.visibility = View.VISIBLE
            trackAdapter.updateTracks(history)
        } else {
            binding.linearLayoutHistory.visibility = View.GONE
            binding.rvHistoryTracks.visibility = View.GONE
        }
        binding.rvHistoryTracks.adapter?.notifyDataSetChanged()
        binding.rvTracks.visibility = View.GONE
    }

    private fun setupListeners() {
        binding.clearHistButton.setOnClickListener {
            searchHistory.clearHistory()
            loadSearchHistory()
        }
    }

    private fun setupTracksRecyclerView() {
        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        binding.rvTracks.adapter = trackAdapter
    }

    private fun setupHistoryRecyclerView() {
        binding.rvHistoryTracks.layoutManager = LinearLayoutManager(this)
        binding.rvHistoryTracks.adapter = trackAdapter
    }


    private fun changeListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.isNullOrEmpty()) {
                    loadSearchHistory()
                    binding.clearImageButton.visibility = View.INVISIBLE
                } else {
                    binding.clearImageButton.visibility = View.VISIBLE
                    binding.linearLayoutHistory.visibility = View.GONE
                }
                restoredText = binding.etSearch.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("etText", restoredText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoredText = savedInstanceState.getString("etText").toString()
    }

    private fun hideKeyboard() {
        currentFocus?.let { view ->
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun loadTrack() {
        ApiFactory.apiService.search(binding.etSearch.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    if (response.isSuccessful) {
                        val tracks = response.body()?.results ?: emptyList()
                        if (tracks.isNotEmpty()) {
                            trackAdapter.updateTracks(tracks)
                            trackAdapter.notifyDataSetChanged()
                            showTrackList()
                        } else {
                            nothingFound()
                        }
                    } else {
                        noInternet()
                    }
                }

                override fun onFailure(p0: Call<TrackResponse>, p1: Throwable) {
                    noInternet()
                }
            })
    }

    private fun showTrackList() {
        with(binding) {
            linearLayoutSearch.visibility = View.GONE
            linearLayoutInternet.visibility = View.GONE
            binding.linearLayoutHistory.visibility = View.GONE
            rvTracks.visibility = View.VISIBLE
        }
    }

    private fun noInternet() {
        with(binding) {
            linearLayoutSearch.visibility = View.GONE
            linearLayoutInternet.visibility = View.VISIBLE
            binding.linearLayoutHistory.visibility = View.GONE
            rvTracks.visibility = View.GONE
        }
    }

    private fun nothingFound() {
        with(binding) {
            linearLayoutSearch.visibility = View.VISIBLE
            linearLayoutInternet.visibility = View.GONE
            binding.linearLayoutHistory.visibility = View.GONE
            rvTracks.visibility = View.GONE
        }
    }
//    private fun showHistoryList() {
//        with(binding) {
//            linearLayoutSearch.visibility = View.GONE
//            linearLayoutInternet.visibility = View.GONE
//            binding.linearLayoutHistory.visibility = View.VISIBLE
//            rvTracks.visibility = View.GONE
//        }
//    }

    private fun refreshButton() {
        binding.refreshButton.setOnClickListener {
            loadTrack()
        }
    }
}
