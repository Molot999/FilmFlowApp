package com.blacksun.filmflow.di

import com.blacksun.filmflow.ui.FilmListViewModel
import com.blacksun.filmflow.data.MovieRepository
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<MovieRepository> { MovieRepository() }
    viewModel { FilmListViewModel(get()) }
}