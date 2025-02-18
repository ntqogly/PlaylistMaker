package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.Response

interface NetworkClient {
    suspend fun searchTrack(dto: Any): Response
}