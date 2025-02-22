package com.blacksun.filmflow.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blacksun.filmflow.R
import com.blacksun.filmflow.data.Movie
import com.blacksun.filmflow.databinding.FragmentFilmDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import kotlin.math.roundToInt


class FilmDetailsFragment : Fragment() {

    companion object {

        private const val ARG_MOVIE = "movie"

        fun newInstance(movie: Movie): FilmDetailsFragment {
            val intent = FilmDetailsFragment()
            intent.arguments = Bundle().apply {
                putParcelable(ARG_MOVIE, movie)
            }
            return intent
        }
    }

    private var _binding: FragmentFilmDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val movie: Movie? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_MOVIE, Movie::class.java)
        } else {
            arguments?.getParcelable(ARG_MOVIE)
        }

        if (movie != null) {
            setupToolbar(movie.localizedName)
            loadMovieData(movie)

        } else {
            parentFragmentManager.popBackStack()
        }

    }

    private fun setupToolbar(movieTitle: String) {
        binding.topAppBar.apply {
            title = movieTitle
            setNavigationOnClickListener { parentFragmentManager.popBackStack() }
        }
    }

    private fun loadMovieData(movie: Movie) {
        loadPoster(movie.imageUrl)
        with(binding) {
            movieTitle.text = movie.localizedName
            movieAddInfo.text = formatMovieInfo(movie)
            movieRatingValue.text = movie.rating.toString()
            movieDescription.text = movie.description
        }
    }

    private fun loadPoster(imageUrl: String) {
        Glide.with(this@FilmDetailsFragment)
            .load(imageUrl)
            .placeholder(R.drawable.movie_img_placeholder)
            .error(R.drawable.movie_img_placeholder)
            .transform(MultiTransformation(CenterCrop(), RoundedCorners(getCornerRadius())))
            .into(binding.moviePoster)
    }

    private fun formatMovieInfo(movie: Movie): String {
        return movie.genres
            .takeIf { it.isNotEmpty() }
            ?.let { genres ->
                getString(R.string.movie_add_info_template, genres.joinToString("/"), movie.year)
            } ?: getString(R.string.movie_year_template, movie.year)
    }

    private fun getCornerRadius(): Int {
        return requireContext().resources.getInteger(R.integer.movie_poster_corner_radius_dp)
            .dpToPx(requireContext())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}