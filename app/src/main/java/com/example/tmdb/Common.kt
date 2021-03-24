package com.example.tmdb

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import java.util.*

object Common {
    const val baseUrl = "https://api.themoviedb.org/"
    const val apiKey = "dbd468dd443c8122522d47916fbc3a59"

    val MAX_RESULT_TO_SHOW = 200

    var language: Locale = Resources.getSystem().configuration.locales[0]
    var emptyImage = ColorDrawable(Color.WHITE)

    init {
        Log.d("sys locale", language.toLanguageTag())
    }
}