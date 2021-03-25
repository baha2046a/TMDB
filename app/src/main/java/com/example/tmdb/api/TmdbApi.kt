package com.example.tmdb.api

import com.example.tmdb.Common
import com.example.tmdb.model.MovieDetail
import com.example.tmdb.model.MovieSearchResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap

interface TmdbApi {
    @GET(Common.MOVIE_SEARCH_API)
    suspend fun searchMovie(@QueryMap params: Map<String, String>): Response<MovieSearchResult>

    @GET(Common.MOVIE_DETAIL_API)
    suspend fun movieDetail(
        @Path(value = "id") id: String,
        @QueryMap params: Map<String, String>
    ): Response<MovieDetail>
}

