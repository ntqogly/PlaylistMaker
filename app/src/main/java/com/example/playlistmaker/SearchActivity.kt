package com.example.playlistmaker

import android.annotation.SuppressLint
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

    @SuppressLint("MissingInflatedId", "SetTextI18n")
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
        binding.backButtonFromSearch.setOnClickListener { finish() }

        if (savedInstanceState != null) {
            binding.etSearch.setText(restoredText)
        }

        loadTrack()

        changeListener()

        refreshButton()
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                loadTrack()
            }
            false
        }
        binding.clearImageButton.setOnClickListener {
            binding.etSearch.setText("")
            hideKeyboard()
            trackList.clear()
        }
        binding.rvTracks.layoutManager = LinearLayoutManager(this)
        binding.rvTracks.adapter = TrackAdapter(trackList)

    }

    private fun changeListener() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //                TODO("Not yet implemented")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearImageButton.visibility =
                    if (s.isNullOrEmpty()) View.INVISIBLE else View.VISIBLE
                restoredText = binding.etSearch.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {
                //                TODO("Not yet implemented")
            }
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
        ApiFactory.getApiService().search(binding.etSearch.text.toString())
            .enqueue(object : Callback<TrackResponse> {
                @SuppressLint("NotifyDataSetChanged")
                override fun onResponse(
                    call: Call<TrackResponse>, response: Response<TrackResponse>
                ) {
                    if (response.isSuccessful) {
                        with(binding) {
                            linearLayoutSearch.visibility = View.GONE
                            linearLayoutInternet.visibility = View.GONE
                            rvTracks.visibility = View.VISIBLE
                        }
                        trackList.clear()
                        val resultResponse = response.body()?.results
                        if (resultResponse?.isNotEmpty() == true) {
                            trackList.addAll(resultResponse)
                            TrackAdapter(trackList).notifyDataSetChanged()
                        } else {
                            with(binding) {
                                linearLayoutSearch.visibility = View.VISIBLE
                                linearLayoutInternet.visibility = View.GONE
                                rvTracks.visibility = View.GONE
                            }
                        }
                    } else {
                        with(binding) {
                            linearLayoutSearch.visibility = View.GONE
                            linearLayoutInternet.visibility = View.VISIBLE
                            rvTracks.visibility = View.GONE
                        }
                    }
                }
                override fun onFailure(p0: Call<TrackResponse>, p1: Throwable) {
                    with(binding) {
                        linearLayoutSearch.visibility = View.GONE
                        linearLayoutInternet.visibility = View.VISIBLE
                        rvTracks.visibility = View.GONE
                    }
                }
            })
    }

    private fun refreshButton() {
        binding.refreshButton.setOnClickListener {
            loadTrack()
        }
    }
}
