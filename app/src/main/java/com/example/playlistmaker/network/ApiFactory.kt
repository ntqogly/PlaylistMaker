package com.example.playlistmaker.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiFactory {

    private const val BASE_URL = "https://itunes.apple.com"

//    private val retrofit =
//        Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(BASE_URL)
//            .build()
//
//    val apiService: ITunesApiService = retrofit.create(ITunesApiService::class.java)

    fun getApiService(): ITunesApiService {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        val gson = GsonBuilder().setLenient().create()

        val retrofit = Retrofit.Builder().addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL).client(client).build()

        return retrofit.create(ITunesApiService::class.java)
    }
}