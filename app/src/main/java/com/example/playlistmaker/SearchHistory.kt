package com.example.playlistmaker

import android.content.SharedPreferences
import com.example.playlistmaker.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    private val key = "search_history"

    fun getHistory(): List<Track> {
        val json = sharedPreferences.getString(key, "[]") ?: "[]"
        val type = object : TypeToken<List<Track>>() {}.type
        val history = Gson().fromJson<List<Track>>(json, type)
        return history
    }

    fun addTrack(track: Track) {
        val history = getHistory().toMutableList()
        history.removeAll { it.trackId == track.trackId }
        history.add(0, track)
        if (history.size > 10) {
            history.removeAt(history.size - 1)
        }
        saveHistory(history)
    }

    fun clearHistory() {
        saveHistory(emptyList())
    }

    private fun saveHistory(history: List<Track>) {
        val json = Gson().toJson(history)
        sharedPreferences.edit().putString(key, json).apply()
    }
}

