package com.blacksun.filmflow.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("films")
    val films: List<Movie>
)