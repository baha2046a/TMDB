package com.example.tmdb.model

import com.example.tmdb.format

/**
 * Data of Movie return by TMDB Search Movie API
 * Use: Bind to [com.example.tmdb.view.MoviesViewAdapter] to Display
 */
data class Movie(
    val adult: Boolean,
    val backdrop_path: String?,
    val genre_ids: List<Int>,
    val id: Int,
    val original_language: String,
    val original_title: String,
    val overview: String,
    val popularity: Double,
    val poster_path: String?,
    val release_date: String,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) {
    val voteAsString: String
        get() = if (vote_average > 0.0) "★${vote_average.format(1)}" else ""

    // Every Movie should have a unique ID
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Movie) return false

        return (id == other.id)
    }

    override fun hashCode(): Int {
        return id
    }
}