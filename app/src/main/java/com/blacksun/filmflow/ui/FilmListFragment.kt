package com.blacksun.filmflow.ui

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.blacksun.filmflow.R
import com.blacksun.filmflow.data.Movie
import com.blacksun.filmflow.data.MovieGenreDisplay
import com.blacksun.filmflow.data.MoviesState
import com.blacksun.filmflow.databinding.FragmentFilmListBinding
import com.blacksun.filmflow.ui.adapter.GenreAdapter
import com.blacksun.filmflow.ui.adapter.MovieAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class FilmListFragment : Fragment() {

    private var _binding: FragmentFilmListBinding? = null
    private val binding get() = _binding!!

    private val filListViewModel: FilmListViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filListViewModel.loadMovies()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmListBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nsvContent.isVisible = false
        binding.loadingIndicator.isVisible = true
        setupObservers()
    }

    private fun setupObservers() {
        observeMoviesState()
    }

    private fun observeMoviesState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                filListViewModel.moviesState.collect { state ->
                    handleMoviesState(state)
                }
            }
        }
    }

    private fun handleMoviesState(state: MoviesState) {
        when (state) {
            is MoviesState.Loading -> {}
            is MoviesState.Success -> showMovies(state.movies)
            is MoviesState.Error -> showError()
        }
    }

    private fun reloadMovies() {
        filListViewModel.loadMovies()
    }

    private fun showMovies(movies: List<Movie>) {
        binding.nsvContent.isVisible = true
        binding.loadingIndicator.isVisible = false

        val movieAdapter = MovieAdapter(
            movies,
            resources.getInteger(R.integer.movie_poster_corner_radius_dp).dpToPx(requireContext())
        ) { movie: Movie ->
            val detailsFragment = FilmDetailsFragment.newInstance(movie)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, detailsFragment)
                .addToBackStack("film_details")
                .commit()
        }

        binding.rvMovies.addItemDecoration(GridSpacingItemDecoration(2, 32, false))
        binding.rvMovies.adapter = movieAdapter

        val genreAdapter = GenreAdapter(
            movieGenreDisplayList = getUniqueGenres(movies),
            onGenreSelected = { movieGenreDisplay: MovieGenreDisplay? ->
                movieAdapter.setGenreFilter(movieGenreDisplay?.displayName)
            }
        )
        binding.rvGenres.adapter = genreAdapter

    }

    private fun showError() {
        binding.loadingIndicator.isVisible = false

        Snackbar.make(
            binding.root,
            getString(R.string.network_connection_error),
            Snackbar.LENGTH_INDEFINITE
        )
            .setAction(getString(R.string.retry)) {
                reloadMovies()
                binding.loadingIndicator.isVisible = true
            }
            .setTextColor(Color.WHITE)
            .setActionTextColor(getColor(requireContext(), R.color.accent_yellow))
            .show()
    }

    private fun getUniqueGenres(movies: List<Movie>): List<MovieGenreDisplay> {
        return movies
            .flatMap { it.genres }
            .distinct()
            .map { originalGenreName ->
                MovieGenreDisplay(
                    displayName = originalGenreName.trim().replaceFirstChar {
                        it.titlecase(Locale.getDefault())
                    },
                    originalName = originalGenreName
                )
            }
            .sortedBy { it.displayName }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}