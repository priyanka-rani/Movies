package com.pri.movies.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pri.movies.data.model.Movie

private const val STARTING_PAGE_INDEX = 1

class UnsplashPagingSource(
    private val service: ApiService
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = service.searchMovies(page)
            val movies = response.search.orEmpty()
            LoadResult.Page(
                data = movies,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if ((response.totalResults ?: 0) <= page * params.loadSize) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            // This loads starting from previous page, but since PagingConfig.initialLoadSize spans
            // multiple pages, the initial load will still load items centered around
            // anchorPosition. This also prevents needing to immediately launch prepend due to
            // prefetchDistance.
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }
}
