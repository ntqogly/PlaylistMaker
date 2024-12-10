package com.example.playlistmaker.data.network

import android.util.Log
import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.data.dto.TrackSearchRequest

class NetworkClientImpl : NetworkClient {
    override fun searchTrack(dto: Any): Response {
        return try {
            if (dto is TrackSearchRequest) {
                val response = ApiFactory.apiService.search(dto.expression).execute()
                response.body()?.apply { resultCode = response.code() } ?: Response(response.code())
            } else {
                return Response(404)
            }
        } catch (e: Exception) {
            Log.e("NO_INTERNET", "NO INTERNET EXCEPTION")
            Response(400)
        }
    }
}