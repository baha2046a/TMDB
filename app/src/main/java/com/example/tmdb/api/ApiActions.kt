package com.example.tmdb.api

import android.util.Log
import com.example.tmdb.Common
import com.example.tmdb.api.TmdbApiService.getSuitableLanguageTag
import com.example.tmdb.model.MovieDetail
import com.example.tmdb.model.MovieSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.HashMap

fun actionSearchMovie(
    keyForValidCheck: String,
    param: Map<String, String>,
    receiver: (String, Optional<MovieSearchResult>) -> Unit
) {
    GlobalScope.launch {
        val response = TmdbApiService.api.searchMovie(param)
        Log.d("API Result", "Response Code : ${response.code()}")
        withContext(Dispatchers.Main) {
            receiver.invoke(keyForValidCheck, Optional.ofNullable(response.body()))
        }
    }
}

suspend fun actionGetMovieDetail(movieId: Int): Pair<Int, Optional<MovieDetail>> {
    val params: MutableMap<String, String> = HashMap()
    params["api_key"] = Common.apiKey
    params["language"] = getSuitableLanguageTag(Common.language)

    val response = TmdbApiService.api.movieDetail(movieId.toString(), params)
    return when (response.isSuccessful) {
        true -> Pair(response.code(), Optional.ofNullable(response.body()))
        false -> Pair(response.code(), Optional.empty())
    }
}

fun String.toTmdbImg(width: Int) = "https://image.tmdb.org/t/p/w$width$this"

fun String.toTmdbImg() = "https://image.tmdb.org/t/p/original$this"

