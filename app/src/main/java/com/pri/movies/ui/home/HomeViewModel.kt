package com.pri.movies.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.pri.movies.combine
import com.pri.movies.data.SearchRepository
import com.pri.movies.data.model.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {
    private val _batmanMovie = MutableLiveData<Unit>()
    private val _latestMovie = MutableLiveData<Unit>()

    init {
        refresh()
    }

    val batmanMovies = _batmanMovie.switchMap {
        repository.getBatmanMovies()
    }

    val latestMovies = _latestMovie.switchMap {
        repository.getLatestMovies()
    }

    val showProgress =
        batmanMovies.combine(latestMovies) { batmanMovieResource, latestMovieResource ->
            batmanMovieResource.status == Status.LOADING ||
                    latestMovieResource.status == Status.LOADING
        }

    private fun getBatmanMovies() {
        _batmanMovie.postValue(Unit)
    }

    private fun getLatestMovies() {
        _latestMovie.postValue(Unit)
    }

    fun refresh() {
        getBatmanMovies()
        getLatestMovies()
    }
}