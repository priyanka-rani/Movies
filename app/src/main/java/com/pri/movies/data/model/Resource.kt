package com.pri.movies.data.model

import com.pri.movies.data.model.Status.*

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
</T> */
data class Resource<T>(
    val status: Status,
    val data: T? = null,
    val message: String? = null
) {
    companion object {
        fun <T> success(data: T?, msg: String? = null): Resource<T> {
            return Resource(SUCCESS, data, msg)
        }

        fun <T> error(data: T? = null, msg: String?): Resource<T> {
            return Resource(ERROR, data, msg)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(LOADING, data)
        }
    }
}
