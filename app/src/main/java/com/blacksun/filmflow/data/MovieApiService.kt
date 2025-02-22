package com.blacksun.filmflow.data

import retrofit2.http.GET

interface MovieApiService {
    @GET("sequeniatesttask/films.json")
    suspend fun getMovies(): MovieResponse
}