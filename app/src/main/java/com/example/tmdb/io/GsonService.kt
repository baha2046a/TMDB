package com.example.tmdb.io

import com.google.gson.Gson
import com.google.gson.GsonBuilder

object GsonService {
    var defaultGson = createDefaultGson()

    private fun createDefaultGson(): Gson {
        return GsonBuilder()
            .setPrettyPrinting()
            .create()
    }
}