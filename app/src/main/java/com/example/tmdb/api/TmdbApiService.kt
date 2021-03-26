package com.example.tmdb.api

import android.util.Log
import com.example.tmdb.Common
import com.example.tmdb.model.MovieDetail
import com.example.tmdb.model.MovieSearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

object TmdbApiService {
    private val supportLanguage: Set<Locale> = setOf(Locale.US, Locale.JAPAN)
    private val api = create()

    fun actionSearchMovie(
        keyForValidCheck: String,
        param: Map<String, String>,
        receiver: (String, Optional<MovieSearchResult>) -> Unit
    ) {
        GlobalScope.launch {
            val response = api.searchMovie(param)
            Log.d("API Search Result", "Response Code : ${response.code()}")
            withContext(Dispatchers.Main) {
                receiver.invoke(keyForValidCheck, Optional.ofNullable(response.body()))
            }
        }
    }

    fun actionGetMovieDetail(movieId: Int, set: (MovieDetail) -> Unit) {
        val params: MutableMap<String, String> = HashMap()
        params["api_key"] = Common.API_KEY
        params["language"] = getSuitableLanguageTag(Common.language)

        GlobalScope.launch {
            val response = api.movieDetail(movieId.toString(), params)
            Log.d("API Detail Result", "Response Code : ${response.code()}")
            if (response.isSuccessful) {
                response.body()?.let {
                    withContext(Dispatchers.Main) {
                        set.invoke(it)
                    }
                }
            }
        }
    }

    fun getSuitableLanguageTag(locale: Locale): String {
        return when (supportLanguage.contains(locale)) {
            true -> locale.toLanguageTag()
            false -> supportLanguage.first().toLanguageTag()
        }
    }

    fun getImageUrl(imgString: String, preferWidth: Int = 0): String {
        val apiPath = when (preferWidth) {
            0 -> Common.MOVIE_IMAGE_API.replace("{width}", "original")
            else -> Common.MOVIE_IMAGE_API.replace("{width}", "w$preferWidth")
        }
        return "${Common.BASE_IMAGE_URL}$apiPath$imgString"
    }

    private fun create(): TmdbApi {
        val baseUrl = Common.BASE_URL
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(TmdbApi::class.java)
    }
}

fun String.toTmdbImg(width: Int = 0) = TmdbApiService.getImageUrl(this, width)