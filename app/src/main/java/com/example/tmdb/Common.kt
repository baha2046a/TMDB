package com.example.tmdb

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.example.tmdb.control.MovieDetailController
import java.util.*
import kotlin.reflect.KClass

object Common {
    /**
     *  Order on how fragment display can be change here (Left to Right Swipe)
     *  Extension (on below) fun KClass<out Fragment>.uiOrder(): Int
     *  will return the Page Index setting here
     */
    val UI_ORDER: HashMap<KClass<out Fragment>, Int> = hashMapOf(
        SettingsFragment::class to 0,
        MainFragment::class to 1,
        DetailFragment::class to 2
    )

    const val BASE_URL = "https://api.themoviedb.org/"
    const val MOVIE_SEARCH_API = "3/search/movie"
    const val MOVIE_DETAIL_API = "3/movie/{id}"

    const val BASE_IMAGE_URL = "https://image.tmdb.org/"
    const val MOVIE_IMAGE_API = "t/p/{width}"

    /**
     * !!! SHOULD SHORE AT SOMEWHERE ELSE e.g. CLOUD !!!
     */
    const val API_KEY = "dbd468dd443c8122522d47916fbc3a59"

    const val FAVORITE_DATA_FILE = "favoriteMovie"

    const val MAX_RESULT_TO_SHOW = 200

    val EMPTY_POSTER = ColorDrawable(Color.DKGRAY)

    // From User Setting
    var ADULT_CONTENT: Boolean = false

    // From User Setting
    var PREFER_LANG: String = "en-US"

    val SYS_LOCALE: Locale = kotlin.runCatching {
        Resources.getSystem().configuration.locales[0]
    }.getOrDefault(Locale.US)

    // Listener to Change of User Preferences
    fun changeListener(sharedPreferences: SharedPreferences, key: String) {
        Log.d("Preferences", "Change $key")
        when (key) {
            "switch_adult" -> ADULT_CONTENT = sharedPreferences.getBoolean(key, false)
            "search_result_lang" -> {
                PREFER_LANG = sharedPreferences.getString(key, "en-US") ?: "en-US"
                MovieDetailController.clearCache()
            }
        }
    }
}

// Extensions

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)

fun KClass<out Fragment>.uiOrder(): Int = Common.UI_ORDER[this] ?: 0