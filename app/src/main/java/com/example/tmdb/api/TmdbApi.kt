package com.example.tmdb.api

import com.example.tmdb.model.MovieDetail
import com.example.tmdb.model.MovieSearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TmdbApi {
    @GET("3/search/movie")
    suspend fun searchMovie(@QueryMap params: Map<String, String>): Response<MovieSearchResult>

    @GET("3/movie/{id}")
    suspend fun movieDetail(
        @Path(value = "id") id: String,
        @QueryMap params: Map<String, String>
    ): Response<MovieDetail>
}