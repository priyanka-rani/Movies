package com.pri.movies.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.pri.movies.data.model.Movie
import com.pri.movies.data.model.Resource
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
}