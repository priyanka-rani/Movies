package com.pri.movies.data

import com.pri.movies.data.model.SearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET(".")
    suspend fun getBatmanMovies(
        @Query("s") s: String = "batman"
    ): SearchResponse

    @GET(".")
    suspend fun getLatestMovies(
        @Query("s") s: String = "batman",
        @Query("y") year: String = "2022",
    ): SearchResponse
}