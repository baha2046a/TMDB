package com.example.tmdb.api

import com.example.tmdb.Common
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object TmdbApiService {
    val api = create()

    private fun create(): TmdbApi {
        val baseUrl = Common.baseUrl
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(TmdbApi::class.java)
    }
}