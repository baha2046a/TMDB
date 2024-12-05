package com.example.tmdb

import android.app.AlertDialog
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.viewpager2.widget.ViewPager2
import com.example.tmdb.control.FavoriteController
import com.example.tmdb.control.MovieDetailController
import com.example.tmdb.control.MovieSearchController
import com.example.tmdb.io.NetworkConnectionInterceptor
import com.example.tmdb.io.NetworkTracker
import com.example.tmdb.view.MovieDetailViewModel
import com.example.tmdb.view.ViewPagerAdapter
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.snackbar.Snackbar
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager2

    lateinit var movieDetailViewModel: MovieDetailViewModel

    var starOn: Drawable? = null
    var starOff: Drawable? = null
    var favoriteButton: MenuItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        starOn = getDrawable(android.R.drawable.btn_star_big_on)
        starOff = getDrawable(android.R.drawable.btn_star_big_off)

        // Show a message on Network Error
        NetworkConnectionInterceptor.onNoNetworkConnection = {
            Snackbar.make(findViewById(R.id.viewpager2), getString(R.string.error_no_network), 2000).show()
        }

        // ViewModel for movie detail
        movieDetailViewModel = ViewModelProvider(this).get(MovieDetailViewModel::class.java)

        // Init viewPager2
        if (!this::viewPager.isInitialized) {
            viewPager = findViewById(R.id.viewpager2)
            viewPager.offscreenPageLimit = Common.UI_ORDER.size
            viewPager.adapter = ViewPagerAdapter(this)
            viewPager.currentItem = DetailFragment::class.uiOrder() // Preload Detail Fragment
            viewPager.currentItem = MainFragment::class.uiOrder() // Show Main Fragment
            Log.d("Init", "Init viewPager2")
        }

        onLoadFavorite()

        // Set User Preferences to Common Variable
        PreferenceManager.getDefaultSharedPreferences(this).apply {
            Common.changeListener(this, "switch_adult")
            Common.changeListener(this, "search_result_lang")
            Common.changeListener(this, "search_result_max")
            Log.d("Init", "Apply User Preference")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        viewPager.currentItem = 0
    }

    override fun onBackPressed() {
        if (viewPager.currentItem == MainFragment::class.uiOrder()) {
            // Ask for Exit or Not
            AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(R.string.close_dialog_title)
                .setMessage(R.string.confirm_exit)
                .setPositiveButton(R.string.yes) { _, _ -> finish() }
                .setNegativeButton(R.string.no, null)
                .show()
        } else {
            // Select the main fragment.
            viewPager.currentItem = MainFragment::class.uiOrder()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        menuInflater.inflate(R.menu.menu_main, menu)

        favoriteButton = menu?.findItem(R.id.action_myFavorite)

        val searchView = menu?.findItem(R.id.action_search)?.actionView as SearchView?
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = onSearchMovie(query)
            override fun onQueryTextChange(newText: String): Boolean = false
        })

        // Fix for a bug that the SearchView in OptionsMenu not full width
        // https://stackoverflow.com/questions/18063103/searchview-in-optionsmenu-not-full-width
        searchView?.maxWidth = Integer.MAX_VALUE
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_myFavorite -> onShowMyFavorite()
            R.id.action_settings -> onShowSetting()
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Show Movie Detail
    fun onClickMovieName(view: View) {
        // MovieID is view.tag
        onShowMovieDetail(view.tag as Int)
    }


    override fun onStart() {
        super.onStart()
        NetworkTracker.register(this)
        if (MovieSearchController.query.isEmpty()) onShowMyFavorite()
    }

    override fun onPause() {
        onSaveFavorite()
        super.onPause()
    }

    override fun onStop() {
        onSaveFavorite()
        NetworkTracker.unregister(this)
        super.onStop()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val lastQuery = savedInstanceState.getString("QueryString")
        if (!lastQuery.isNullOrEmpty()) MovieSearchController.query = lastQuery
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("QueryString", MovieSearchController.query)
    }

    private fun onLoadFavorite() = FavoriteController.load(this)

    private fun onSaveFavorite() = FavoriteController.save(this)

    fun onSearchMovie(query: String): Boolean {
        MovieSearchController.query = query
        hideKeyboard()
        viewPager.currentItem = MainFragment::class.uiOrder()
        setTitle(R.string.app_name)
        starOff.let {
            favoriteButton?.setIcon(it)
        }
        return true
    }


    private fun onShowMovieDetail(movieId: Int) {
        MovieDetailController.getDetail(movieId) {
            hideKeyboard()
            movieDetailViewModel.set(it)
            viewPager.currentItem = DetailFragment::class.uiOrder()
        }
    }

    private fun onShowMyFavorite(): Boolean {
        MovieSearchController.query = ""
        MovieSearchController.setResult(
            "",
            Optional.of(FavoriteController.asMovieSearchResult())
        )
        viewPager.currentItem = MainFragment::class.uiOrder()
        hideKeyboard()
        setTitle(R.string.action_favorite)
        starOn.let {
            favoriteButton?.setIcon(it)
        }
        return true
    }

    private fun onShowSetting(): Boolean {
        viewPager.currentItem = SettingsFragment::class.uiOrder()
        return true
    }
}