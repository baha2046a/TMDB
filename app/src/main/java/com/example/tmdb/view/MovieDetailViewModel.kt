package com.example.tmdb.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tmdb.model.MovieDetail

class MovieDetailViewModel : ViewModel() {
    private val _movieDetail = MutableLiveData<MovieDetail>()

    val movieDetail: LiveData<MovieDetail>
        get() = _movieDetail

    fun set(detail: MovieDetail) {
        _movieDetail.value = detail
    }
}