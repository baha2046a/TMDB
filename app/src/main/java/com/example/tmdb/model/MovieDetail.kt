package com.example.tmdb.model

data class MovieDetail(
    val adult: Boolean,
    val backdrop_path: String?,
    val belongs_to_collection: Any?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val id: Int,
    val imdb_id: String?,
    val original_language: String,
    val original_title: String,
    val overview: String?,
    val popularity: Double,
    val poster_path: String?,
    val production_companies: List<ProductionCompany>,
    val production_countries: List<ProductionCountry>,
    val release_date: String,
    val revenue: Int,
    val runtime: Int,
    val spoken_languages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val title: String,
    val video: Boolean,
    val vote_average: Double,
    val vote_count: Int
) {
    val languageString: String
        get() = spoken_languages.map { it.name }.reduceOrNull { r, t -> "$r $t" } ?: ""

    val popularityString: String
        get() = popularity.format(2)

    val voteAverageString: String
        get() = "★ " + vote_average.format(2) + " (\uD83D\uDC64$vote_count)"

    private fun Double.format(digits: Int) = "%.${digits}f".format(this)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MovieDetail) return false

        return (id == other.id)
    }

    override fun hashCode(): Int {
        return id
    }
}