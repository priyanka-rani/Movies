package com.pri.movies.ui.listing

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pri.movies.R
import com.pri.movies.data.model.Movie
import com.pri.movies.databinding.ItemMovieListBinding
import com.pri.movies.databinding.LayoutPagerLoadingBinding
import com.squareup.picasso.Picasso

class MoviePagingAdapter(private val onItemClick: (Movie) -> Unit) :
    PagingDataAdapter<Movie, MoviePagingAdapter.ViewHolder>(Movie.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMovieListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
        }
    }

    inner class ViewHolder(
        private val binding: ItemMovieListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val item = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                onItemClick(item)
            }
        }

        fun bind(item: Movie) {
            with(binding) {
                tvTitle.text = item.title
                Picasso.get()
                    .load(item.poster)
                    .placeholder(R.drawable.movie_placeholder)
                    .error(R.drawable.movie_placeholder)
                    .into(ivThumb)
            }
        }
    }
}

class PagingLoaderAdapter : LoadStateAdapter<PagingLoaderAdapter.ViewHolder>() {
    override fun onBindViewHolder(
        holder: ViewHolder, loadState: LoadState
    ) {
        //nop
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, loadState: LoadState
    ): ViewHolder = ViewHolder(
        LayoutPagerLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ViewHolder(binding: LayoutPagerLoadingBinding) :
        RecyclerView.ViewHolder(binding.root)

}
