package com.example.tmdb.api

import com.example.tmdb.Common
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*

object TmdbApiService {
    val supportLanguage: Set<Locale> = setOf(Locale.US, Locale.JAPAN)
    val api = create()

    fun getSuitableLanguageTag(locale: Locale): String {
        return when (supportLanguage.contains(locale)) {
            true -> locale.toLanguageTag()
            false -> supportLanguage.first().toLanguageTag()
        }
    }

    private fun create(): TmdbApi {
        val baseUrl = Common.baseUrl
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        return retrofit.create(TmdbApi::class.java)
    }
}