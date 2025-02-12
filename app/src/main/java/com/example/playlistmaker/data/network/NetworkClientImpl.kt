package com.example.playlistmaker.data.network

import android.util.Log
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest

class NetworkClientImpl(private val apiService: ITunesApiService) : NetworkClient {
    override suspend fun searchTrack(dto: Any): Response {
        return try {
            if (dto is TrackSearchRequest) {
                val response = apiService.search(dto.expression)
                response.apply { resultCode = 200 }
            } else {
                return Response(404)
            }
        } catch (e: Exception) {
            Log.e("NO_INTERNET", "NO INTERNET EXCEPTION")
            Response(400)
        }
    }
}