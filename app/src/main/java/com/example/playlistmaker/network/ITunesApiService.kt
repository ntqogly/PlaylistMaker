package com.example.playlistmaker.network

import com.example.playlistmaker.models.TrackResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApiService {

    @GET("/search?entity=song")
    fun search(@Query(PARAM_NAME) text: String): Call<TrackResponse>

    companion object {
        private const val PARAM_NAME = "term"
    }
}



