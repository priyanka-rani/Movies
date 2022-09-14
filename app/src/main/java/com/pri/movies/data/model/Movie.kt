package com.pri.movies.data.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    @field:SerializedName("Poster")
    val poster: String? = null,
    @field:SerializedName("Title")
    val title: String? = null,
    @field:SerializedName("Type")
    val type: String? = null,
    @field:SerializedName("Year")
    val year: String? = null,
    @field:SerializedName("imdbID")
    val imdbID: String? = null
) : Parcelable {
    override fun toString(): String {
        return title.orEmpty()
    }
    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem.imdbID == newItem.imdbID
            }

            override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
                return oldItem == newItem
            }
        }

    }
}
