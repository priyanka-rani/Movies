package com.pri.movies.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pri.movies.R
import com.pri.movies.data.model.Movie
import com.pri.movies.databinding.ItemMovieBinding
import com.squareup.picasso.Picasso

class MovieAdapter(private val onItemClick: (Movie) -> Unit) :
    ListAdapter<Movie, MovieAdapter.ViewHolder>(Movie.DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class ViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val item = getItem(adapterPosition)
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