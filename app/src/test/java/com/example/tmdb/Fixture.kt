package com.example.tmdb

import com.example.tmdb.model.Movie
import com.example.tmdb.model.MovieSearchResult

object Fixture {
    fun genSearchResult(): MovieSearchResult {
        // TODO: Result size should be random
        return MovieSearchResult(1, listOf(genMovie(1), genMovie(12)), 1, 2)
    }

    fun genMovie(id: Int): Movie {
        // TODO: Field value should random
        return Movie(
            true, null, listOf(), id, "",
            "", "", 1.0, null, "", "T$id",
            true, 1.0, 5
        )
    }
}