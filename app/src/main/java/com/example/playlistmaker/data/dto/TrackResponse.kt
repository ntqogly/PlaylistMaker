package com.example.playlistmaker.data.dto

import com.google.gson.annotations.SerializedName

class TrackResponse(
    resultCode: Int,
    @SerializedName("results") val results: List<TrackDTO>
) : Response(resultCode)
