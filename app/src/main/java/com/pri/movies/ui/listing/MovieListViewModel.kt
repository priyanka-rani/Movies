package com.pri.movies.ui.listing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pri.movies.data.SearchRepository
import com.pri.movies.data.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {
    init {
        refresh()
    }

    var currentSearchResult: Flow<PagingData<Movie>>? = null

    private fun getMovies() {
        val newResult: Flow<PagingData<Movie>> =
            repository.getSearchResultStream().cachedIn(viewModelScope)
        currentSearchResult = newResult
    }

    fun refresh() {
        getMovies()
    }
}