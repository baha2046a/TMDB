package com.example.tmdb.control

import com.example.tmdb.model.Movie

object FavoriteController {
    private val favoriteList: MutableSet<Movie> = mutableSetOf()

    fun add(movie: Movie) = favoriteList.add(movie)

    fun remove(movie: Movie) = favoriteList.remove(movie)

    fun isExist(movie: Movie): Boolean = favoriteList.contains(movie)

    fun save() {}
}