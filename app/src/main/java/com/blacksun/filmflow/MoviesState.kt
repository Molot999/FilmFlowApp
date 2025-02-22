package com.blacksun.filmflow

import com.blacksun.filmflow.data.Movie

sealed class MoviesState {
    data object Loading : MoviesState()
    data class Success(val movies: List<Movie>) : MoviesState()
    data class Error(val message: String) : MoviesState()
}