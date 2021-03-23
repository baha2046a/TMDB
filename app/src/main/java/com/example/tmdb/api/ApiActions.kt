package com.example.tmdb.api

import com.example.tmdb.Common
import com.example.tmdb.model.MovieDetail
import com.example.tmdb.model.MovieSearchResult
import java.util.*
import kotlin.collections.HashMap

suspend fun actionSearchMovie(query: String, page: Int = 1): Pair<Int, Optional<MovieSearchResult>> {
    if (page !in 1..1000 || query.isEmpty()) return Pair(404, Optional.empty())

    val params: MutableMap<String, String> = HashMap()
    params["api_key"] = Common.apiKey
    params["language"] = Common.language
    params["page"] = page.toString()
    params["query"] = query

    val response = TmdbApiService.api.searchMovie(params)
    return when (response.isSuccessful) {
        true -> Pair(response.code(), Optional.ofNullable(response.body()))
        false -> Pair(response.code(), Optional.empty())
    }
}

suspend fun actionGetMovieDetail(movieId: Int): Pair<Int, Optional<MovieDetail>> {
    val params: MutableMap<String, String> = HashMap()
    params["api_key"] = Common.apiKey
    params["language"] = Common.language

    val response = TmdbApiService.api.movieDetail(movieId.toString(), params)
    return when (response.isSuccessful) {
        true -> Pair(response.code(), Optional.ofNullable(response.body()))
        false -> Pair(response.code(), Optional.empty())
    }
}

