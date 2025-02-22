package com.example.playlistmaker.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.playlistmaker.data.db.FavoriteTrackDao
import com.example.playlistmaker.data.dto.TrackResponse
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.mapper.TrackMapper
import com.example.playlistmaker.domain.api.TrackRepository
import com.example.playlistmaker.domain.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val trackMapper: TrackMapper,
    private val context: Context,
    private val favoriteTrackDao: FavoriteTrackDao
) : TrackRepository {

    //    override fun searchTrack(expression: String): Flow<List<Track>> = flow {
//        val response = withContext(Dispatchers.IO) { networkClient.searchTrack(TrackSearchRequest(expression)) }
//        if (response.resultCode == 200 && response is TrackResponse) {
//            emit(trackMapper.mapToDomainList(response.results))
//        } else {
//            emit(emptyList())
//        }
//    }
    override fun searchTrack(expression: String): Flow<List<Track>> = flow {
        val response =
            withContext(Dispatchers.IO) { networkClient.searchTrack(TrackSearchRequest(expression)) }
        if (response.resultCode == 200 && response is TrackResponse) {
            val trackList = trackMapper.mapToDomainList(response.results)
            val favoriteTrackIds =
                withContext(Dispatchers.IO) { favoriteTrackDao.getFavoriteTrackIds() }.toSet()
            trackList.forEach { track ->
                track.isFavorite = favoriteTrackIds.contains(track.trackId.toLong())
            }

            emit(trackList)
        } else {
            emit(emptyList())
        }
    }


    override fun isInternetAvailable(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val networkCapabilities =
            connectivityManager.getNetworkCapabilities(network) ?: return false
        return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }
}