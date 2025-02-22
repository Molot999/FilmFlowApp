package com.blacksun.filmflow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blacksun.filmflow.data.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FilmListViewModel(
    private val movieRepository: MovieRepository
): ViewModel() {

    private val _moviesState = MutableStateFlow<MoviesState>(MoviesState.Loading)
    val moviesState: StateFlow<MoviesState> = _moviesState

    fun loadMovies() {
        viewModelScope.launch {
            _moviesState.value = MoviesState.Loading
            try {
                val movies = movieRepository.getMovies()
                _moviesState.value = if (movies.isNotEmpty()) {
                    MoviesState.Success(movies)
                } else {
                    MoviesState.Error("No movies found")
                }
            } catch (e: Exception) {
                _moviesState.value = MoviesState.Error(e.message ?: "Unknown error")
            }
        }
    }

}