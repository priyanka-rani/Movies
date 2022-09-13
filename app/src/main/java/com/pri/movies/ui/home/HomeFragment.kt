package com.pri.movies.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.pri.movies.R
import com.pri.movies.data.model.Movie
import com.pri.movies.databinding.FragmentHomeBinding
import com.pri.movies.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private val viewModel by viewModels<HomeViewModel>()

    private val batmanMovieAdapter by lazy { MovieAdapter(::onMovieClick) }
    private val latestMovieAdapter by lazy { MovieAdapter(::onMovieClick) }

    override fun getBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            rvBatmanMovies.adapter = batmanMovieAdapter
            rvLatestMovies.adapter = latestMovieAdapter

            val imageList =
                MoviePosters.map { SlideModel(imagePath = it, scaleType = ScaleTypes.FIT) }
            bannerView.setImageList(imageList)

            tvBatmanSeeAll.setOnClickListener {  }
            tvLatestSeeAll.setOnClickListener {  }

            swipeRefreshLayout.setOnRefreshListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    delay(1000)
                    whenResumed {
                        swipeRefreshLayout.isRefreshing = false
                    }
                }
                viewModel.refresh()
            }

            observeLiveData()
        }
    }

    private fun FragmentHomeBinding.observeLiveData() {
        with(viewModel) {
            batmanMovies.observe(viewLifecycleOwner) {
                it?.data?.let { movieList ->
                    batmanMovieAdapter.submitList(movieList)
                } ?: kotlin.run {
                    it?.message?.let { message ->
                        showToast(message)
                    }
                }
            }
            latestMovies.observe(viewLifecycleOwner) {
                it?.data?.let { movieList ->
                    latestMovieAdapter.submitList(movieList)
                } ?: kotlin.run {
                    it?.message?.let { message ->
                        showToast(message)
                    }
                }
            }
            showProgress.observe(viewLifecycleOwner) {
                it?.let { show ->
                    progressBar.isVisible = show
                }
            }
        }
    }

    private fun onMovieClick(movie: Movie) {
        // TODO: go to movie details page
    }

    companion object {
        private val MoviePosters = listOf(
            R.drawable.movie_1,
            R.drawable.movie_2,
            R.drawable.movie_3,
            R.drawable.movie_4,
            R.drawable.movie_5
        )
    }
}