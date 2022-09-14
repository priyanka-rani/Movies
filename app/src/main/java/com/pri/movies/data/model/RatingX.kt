package com.pri.movies.data.model


import com.google.gson.annotations.SerializedName

data class RatingX(
    @field:SerializedName("Source")
    val source: String? = null,
    @field:SerializedName("Value")
    val value: String? = null
)