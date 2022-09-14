package com.pri.movies.ui.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pri.movies.data.SearchRepository
import com.pri.movies.data.model.Movie
import com.pri.movies.data.model.MovieDetailsResponse
import com.pri.movies.data.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val repository: SearchRepository
) : ViewModel() {

    fun getMovieDetailsResponse(imdbId: String): LiveData<Resource<MovieDetailsResponse>> {
        return repository.getMovieDetails(imdbId)
    }
}