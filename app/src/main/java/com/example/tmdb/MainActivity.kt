package com.example.tmdb

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.tmdb.control.FavoriteController
import com.example.tmdb.control.MovieDetailController
import com.example.tmdb.control.MovieSearchController
import com.example.tmdb.view.ViewPagerAdapter
import com.facebook.drawee.backends.pipeline.Fresco


class MainActivity : AppCompatActivity() {
    lateinit var viewPager: ViewPager2
    lateinit var detailFragment: DetailFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        viewPager = findViewById(R.id.viewpager2)
        viewPager.adapter = ViewPagerAdapter(this).apply {
            this.fragmentList.add(MainFragment().apply {
                arguments = Bundle().apply {
                    putInt("object", 1)
                }
            })
            this.fragmentList.add(DetailFragment().apply {
                arguments = Bundle().apply {
                    putInt("object", 2)
                }.also { detailFragment = this }
            })
        }
        viewPager.currentItem = 0

        onLoadFavorite()

        MovieSearchController.query = "Hero"

    }

    // Show Movie Detail
    fun onClickMovieName(view: View) {
        Log.d("GET Detail", view.tag.toString())

        MovieDetailController.getDetail(view.tag as Int) {
            viewPager.currentItem = 1
            detailFragment.setItem(it)
        }
    }

    override fun onPause() {
        onSaveFavorite()
        super.onPause()
    }

    override fun onStop() {
        onSaveFavorite()
        super.onStop()
    }

    private fun onLoadFavorite() = FavoriteController.load(this)

    private fun onSaveFavorite() = FavoriteController.save(this)
}