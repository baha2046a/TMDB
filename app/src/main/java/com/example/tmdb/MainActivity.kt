package com.example.tmdb

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.control.MovieSearchController
import com.example.tmdb.view.MoviesAdapter
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Fresco.initialize(this)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))

        initRecyclerView()

        MovieSearchController.query = "Hero"

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun initRecyclerView() {
        val layoutManager = GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)

        val gridRecyclerView = findViewById<RecyclerView>(R.id.movieGridView).apply {
            this.layoutManager = layoutManager
            this.setHasFixedSize(false)
        }

        MoviesAdapter(MovieSearchController.resultSet).apply {
            gridRecyclerView.adapter = this
            MovieSearchController.onReset = this::notifyDataSetChanged
            MovieSearchController.onNewData = this::notifyItemRangeInserted
        }

        gridRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!MovieSearchController.complete && !MovieSearchController.loading &&
                    layoutManager.findLastCompletelyVisibleItemPosition() == MovieSearchController.resultSet.size - 3
                ) {
                    MovieSearchController.loadNextPage()
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}