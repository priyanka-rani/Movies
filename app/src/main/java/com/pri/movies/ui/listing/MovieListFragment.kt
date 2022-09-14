package com.pri.movies.ui.listing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.whenResumed
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.pri.movies.data.model.Movie
import com.pri.movies.databinding.FragmentMovieListBinding
import com.pri.movies.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : BaseFragment<FragmentMovieListBinding>() {
    private val viewModel by viewModels<MovieListViewModel>()
    private val adapter by lazy { MoviePagingAdapter(::onMovieClick) }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMovieListBinding {
        return FragmentMovieListBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val loaderAdapter = PagingLoaderAdapter()
        binding.rvMovieList.adapter = adapter.withLoadStateFooter(loaderAdapter)
        val gridLayoutManager = binding.rvMovieList.layoutManager as GridLayoutManager
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == adapter.itemCount && loaderAdapter.itemCount > 0) {
                    2
                } else {
                    1
                }
            }
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewLifecycleOwner.lifecycleScope.launch {
                delay(1000)
                whenResumed {
                    binding.swipeRefreshLayout.isRefreshing = false
                }
            }
            viewModel.refresh()
        }
        lifecycleScope.launch {
            viewModel.currentSearchResult?.collectLatest {
                adapter.submitData(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            adapter.loadStateFlow.collectLatest { loadStates ->
                val showProgress = loadStates.refresh is LoadState.Loading
                binding.progressBar.isVisible = showProgress
            }
        }

    }

    private fun onMovieClick(movie: Movie) {
        navigate(MovieListFragmentDirections.actionListFragmentToMovieDetailsFragment(movie = movie))
    }

}