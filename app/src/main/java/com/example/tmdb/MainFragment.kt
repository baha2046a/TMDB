package com.example.tmdb

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.control.MovieSearchController
import com.example.tmdb.view.MoviesViewAdapter
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class MainFragment : Fragment() {
    lateinit var searchResultView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!this::searchResultView.isInitialized) initRecyclerView(view)
    }

    private fun initRecyclerView(view: View) {
        val layoutManager = FlexboxLayoutManager(view.context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.flexWrap = FlexWrap.WRAP

        searchResultView = view.findViewById<RecyclerView>(R.id.movie_grid_view).apply {
            this.layoutManager = layoutManager
            this.setHasFixedSize(true)
        }

        MoviesViewAdapter(MovieSearchController.resultSet).apply {
            searchResultView.adapter = this
            MovieSearchController.onReset = this::notifyDataSetChanged
            MovieSearchController.onNewData = this::notifyItemRangeInserted
        }

        searchResultView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // When scroll reach last few items, inform controller to load next page
                if (!MovieSearchController.complete && !MovieSearchController.loading &&
                    layoutManager.findLastVisibleItemPosition() >= MovieSearchController.resultSet.size - 4
                ) {
                    if (MovieSearchController.loadNextPage()) {
                        Snackbar.make(recyclerView, getString(R.string.view_loading_next_page), 1000).show()
                    }
                }
            }
        })
    }
}