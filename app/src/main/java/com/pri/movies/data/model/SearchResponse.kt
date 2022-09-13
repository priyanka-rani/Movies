package com.pri.movies.data.model

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @field:SerializedName("Search")
    val search: List<Movie>? = null,
    @field:SerializedName("totalResults")
    val totalResults: Int? = null,
    @field:SerializedName("Response")
    val response: Boolean = false,
    @field:SerializedName("Error")
    val error: String? = null
) {
    fun toResource(): Resource<List<Movie>> {
        return if (response) Resource.success(data = search)
        else Resource.error(msg = error)
    }
}
