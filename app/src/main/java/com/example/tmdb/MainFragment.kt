package com.example.tmdb

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tmdb.control.FavoriteController
import com.example.tmdb.control.MovieSearchController
import com.example.tmdb.view.MoviesViewAdapter
import com.google.android.material.snackbar.Snackbar
import java.util.*

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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.menu_main, menu)

        val searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean = onSearchMovie(query)
            override fun onQueryTextChange(newText: String): Boolean = false
        })

        // Fix for a bug that the SearchView in OptionsMenu not full width
        // https://stackoverflow.com/questions/18063103/searchview-in-optionsmenu-not-full-width
        searchView.maxWidth = Integer.MAX_VALUE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_myFavorite -> onShowMyFavorite()
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView(view)
    }

    private fun initRecyclerView(view: View) {
        val layoutManager = GridLayoutManager(view.context, 2, GridLayoutManager.VERTICAL, false)

        searchResultView = view.findViewById<RecyclerView>(R.id.movie_grid_view).apply {
            this.layoutManager = layoutManager
            this.setHasFixedSize(false)
        }

        MoviesViewAdapter(MovieSearchController.resultSet).apply {
            searchResultView.adapter = this
            MovieSearchController.onReset = this::notifyDataSetChanged
            MovieSearchController.onNewData = this::notifyItemRangeInserted
        }

        searchResultView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // When scroll reach last 3 items, inform controller to load next page
                if (!MovieSearchController.complete && !MovieSearchController.loading &&
                    layoutManager.findLastCompletelyVisibleItemPosition() == MovieSearchController.resultSet.size - 3
                ) {
                    if (MovieSearchController.loadNextPage()) {
                        Snackbar.make(recyclerView, getString(R.string.view_loading_next_page), 1000).show()
                    }
                }
            }
        })
    }

    private fun onSearchMovie(query: String): Boolean {
        MovieSearchController.query = query
        hideKeyboard()
        activity?.setTitle(R.string.action_search)
        return true
    }

    private fun onShowMyFavorite(): Boolean {
        MovieSearchController.query = ""
        MovieSearchController.setResult(
            "",
            Optional.of(FavoriteController.asMovieSearchResult())
        )
        hideKeyboard()
        activity?.setTitle(R.string.action_favorite)
        return true
    }
}