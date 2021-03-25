package com.example.tmdb.control

import android.content.Context
import com.example.tmdb.Common
import com.example.tmdb.io.FilesIO
import com.example.tmdb.model.Movie
import com.example.tmdb.model.MovieSearchResult

object FavoriteController {
    private var favoriteList: MutableSet<Movie> = mutableSetOf()

    fun add(movie: Movie) = favoriteList.add(movie)

    fun remove(movie: Movie) = favoriteList.remove(movie)

    fun isExist(movie: Movie): Boolean = favoriteList.contains(movie)

    fun asMovieSearchResult() = MovieSearchResult(1, favoriteList.toList(), 1, favoriteList.size)

    fun save(context: Context) {
        FilesIO.writeClassToFile(context, favoriteList, Common.FAVORITE_DATA_FILE)
    }

    fun load(context: Context) {
        favoriteList = FilesIO.readClassFromFile(context, Common.FAVORITE_DATA_FILE, mutableSetOf())
    }
}