package com.example.tmdb.model

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