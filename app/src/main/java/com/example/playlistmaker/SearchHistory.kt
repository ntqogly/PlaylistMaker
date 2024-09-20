package com.example.playlistmaker

import android.content.Context
import android.view.View
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(
    context: Context,
    private val rvSearchHist: RecyclerView,
    private val clearHistButton: Button,
    private val tvSearchHist: View
) {
    private val trackList = ArrayList<Track>()
    private val sharedPrefs = context.getSharedPreferences("history", Context.MODE_PRIVATE)
    private var histAdapter = TrackAdapter(trackList)

    init {
        val adapter = TrackAdapter(emptyList())
        adapter.onAddTrack = { track ->
            addNewTrackToHistList(track)
        }
        rvSearchHist.adapter = histAdapter
        rvSearchHist.layoutManager = LinearLayoutManager(context)
        clearHistButton.setOnClickListener {
            clearHist()
        }
        loadSearchHist()
    }

    private fun searchHist(): List<Track> {
        val json = sharedPrefs.getString("history", null) ?: return emptyList()
        val type = object : TypeToken<List<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    private fun saveSearchHist(hist: List<Track>) {
        val json = Gson().toJson(hist)
        sharedPrefs.edit().putString("history", json).apply()
    }

    private fun isHist(): Boolean {
        return searchHist().isNotEmpty()
    }

    private fun hideHistList() {
        rvSearchHist.visibility = View.GONE
        clearHistButton.visibility = View.GONE
        tvSearchHist.visibility = View.GONE
    }

    private fun clearHist() {
        saveSearchHist(emptyList())
        hideHistList()
    }

    private fun loadSearchHist() {
        if (isHist()) {
            val hist = searchHist()
            histAdapter.refreshTrackList(hist)
            rvSearchHist.visibility = View.VISIBLE
            clearHistButton.visibility = View.VISIBLE
            tvSearchHist.visibility = View.VISIBLE
        } else {
            hideHistList()
        }
    }

    private fun addNewTrackToHistList(track: Track) {
        val hist = searchHist().toMutableList()
        hist.remove(track)
        hist.add(0, track)
        if (hist.size > 10) {
            hist.removeAt(hist.size - 1)
        }
        saveSearchHist(hist)
        loadSearchHist()
    }

}