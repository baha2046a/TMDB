package com.example.tmdb.control

import android.util.Log
import com.example.tmdb.Common
import com.example.tmdb.api.TmdbApiService
import com.example.tmdb.api.actionSearchMovie
import com.example.tmdb.model.Movie
import com.example.tmdb.model.MovieSearchResult
import java.util.*
import kotlin.collections.HashMap

object MovieSearchController {
    /**
     * Setting query to a non empty value will start a search in TMDB
     */
    var query: String = ""
        set(value) {
            if (field != value) {
                field = value
                reset()
            }
        }

    /**
     * Full list of the result movie, start at page 1 and auto expand when call loadNextPage()
     **/
    val resultSet: MutableList<Movie> = mutableListOf()

    // Should be set to inform the Adapter there has a update
    var onNewData: (Int, Int) -> Unit = { _, _ -> }
    var onReset: () -> Unit = { }

    var loading: Boolean = false
        private set

    val complete: Boolean
        get() = (currentPage >= totalPage || resultSet.size >= Common.MAX_RESULT_TO_SHOW)

    private val queryParam: MutableMap<String, String> = HashMap()
    private val valid: Boolean
        get() = query.isNotEmpty()

    private var currentPage: Int = 0
    private var totalPage: Int = 1

    init {
        queryParam["api_key"] = Common.apiKey
        queryParam["language"] = TmdbApiService.getSuitableLanguageTag(Common.language)
    }

    /**
     * Load next page result if there has one
     **/
    fun loadNextPage() {
        val nextPage = currentPage + 1
        if (!loading && valid && !complete && nextPage <= totalPage) {
            synchronized(this) {
                if (!loading) {
                    loading = true
                    queryParam["page"] = nextPage.toString()
                    Log.d(this::class.simpleName, "GET search $query page $nextPage")
                    actionSearchMovie(query, queryParam, MovieSearchController::setResult)
                }
            }
        }
    }

    /**
     * query is changed
     **/
    private fun reset() {
        currentPage = 0
        totalPage = 1
        resultSet.clear()
        onReset.invoke()
        loading = false
        queryParam["query"] = query
        loadNextPage()
    }

    /**
     * TMDB API return something, handle it
     **/
    private fun setResult(resultQuery: String, resultData: Optional<MovieSearchResult>) {
        Log.d(this::class.simpleName, "Receive Result for $resultQuery")
        // If query changed before result come back, then this result should be ignore
        if (resultQuery == query) {
            resultData.ifPresent {
                currentPage = it.page
                totalPage = it.total_pages
                val oldSize = resultSet.size

                // Result from the database may change between our call for different page
                // For example: DB: 1-12
                //              Get Page 1 -> 1-10
                //              Information in DB change: (new 1)+(1-12) = 1-13
                //              Get Page 2 -> new 11-13 but the 11 is same as last 10
                // Have to check to avoid adding dup movie in the list
                it.results.forEach { movie ->
                    if (!resultSet.contains(movie)) resultSet.add(movie)
                }

                val newItemCount = resultSet.size - oldSize
                onNewData.invoke(oldSize, newItemCount)
                Log.d(this::class.simpleName, "$newItemCount results Added")
            }
        }
        loading = false
    }
}