package com.example.tmdb.control

import android.util.Log
import com.example.tmdb.api.TmdbApiService
import com.example.tmdb.model.MovieDetail

object MovieDetailController {
    private val detailCache: MutableMap<Int, MovieDetail> = hashMapOf()

    fun getDetail(movieId: Int, set: (MovieDetail) -> Unit) {
        detailCache[movieId]?.let {
            set.invoke(it)
            return
        }

        Log.d(this::class.simpleName, "GET detail : MovieId $movieId")

        TmdbApiService.actionGetMovieDetail(movieId) {
            detailCache[movieId] = it
            set.invoke(it)
        }
    }
}