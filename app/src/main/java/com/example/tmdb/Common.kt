package com.example.tmdb

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import java.util.*

object Common {
    const val BASE_URL = "https://api.themoviedb.org/"
    const val MOVIE_SEARCH_API = "3/search/movie"
    const val MOVIE_DETAIL_API = "3/movie/{id}"

    const val BASE_IMAGE_URL = "https://image.tmdb.org/"
    const val MOVIE_IMAGE_API = "t/p/{width}"

    const val API_KEY = "dbd468dd443c8122522d47916fbc3a59"

    const val FAVORITE_DATA_FILE = "favoriteMovie"

    const val MAX_RESULT_TO_SHOW = 200

    val EMPTY_POSTER = ColorDrawable(Color.DKGRAY)

    var language: Locale = kotlin.runCatching {
        Resources.getSystem().configuration.locales[0]
    }.getOrDefault(Locale.US)

    init {
        language = Locale.JAPAN
        Log.d("System Locale", language.toLanguageTag())
    }
}