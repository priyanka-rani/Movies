package com.pri.movies.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.pri.movies.data.model.Movie
import com.pri.movies.data.model.MovieDetailsResponse
import com.pri.movies.data.model.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SearchRepository @Inject constructor(
    private val apiService: ApiService
) {
    private fun <T> getApiData(apiResponse: suspend () -> Resource<T>): LiveData<Resource<T>> {
        return liveData {
            emit(Resource.loading())
            try {
                val result = apiResponse.invoke()
                emit(result)
            } catch (e: Throwable) {
                emit(Resource.error(msg = e.message))
            }
        }
    }

    fun getLatestMovies(): LiveData<Resource<List<Movie>>> {
        return getApiData {
            apiService.getLatestMovies().toResource()
        }
    }

    fun getBatmanMovies(): LiveData<Resource<List<Movie>>> {
        return getApiData {
            apiService.getBatmanMovies().toResource()
        }
    }

    fun getSearchResultStream(): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                enablePlaceholders = false,
                pageSize = NETWORK_PAGE_SIZE,
                prefetchDistance = 2
            ),
            pagingSourceFactory = { MoviePagingSource(apiService) }
        ).flow
    }

    fun getMovieDetails(imdbId:String): LiveData<Resource<MovieDetailsResponse>> {
        return getApiData {
            apiService.getMovieDetails(imdbId).toResource()
        }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 10
    }
}