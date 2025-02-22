package com.blacksun.filmflow.data

class MovieRepository {
    suspend fun getMovies(): List<Movie> {
        return try {
            val response = RetrofitClient.movieApi.getMovies()
            response.films
        } catch (e: Exception) {
            emptyList()
        }
    }
}