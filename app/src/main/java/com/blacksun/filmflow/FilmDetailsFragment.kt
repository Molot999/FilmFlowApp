package com.blacksun.filmflow

import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.fragment.app.Fragment
import com.blacksun.filmflow.data.Movie
import com.blacksun.filmflow.databinding.FragmentFilmDetailsBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.target.BitmapImageViewTarget
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topAppBar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val movie: Movie? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            arguments?.getParcelable(ARG_MOVIE, Movie::class.java)
        } else {
            arguments?.getParcelable(ARG_MOVIE)
        }

        if (movie == null) {
            return
        }

        Glide.with(this@FilmDetailsFragment)
            .load(movie.imageUrl)
            .placeholder(R.drawable.movie_img_placeholder)
            .error(R.drawable.movie_img_placeholder)
            .transform(
                MultiTransformation(
                    RoundedCorners(8.dpToPx(requireContext())),
                    CenterCrop()
                )
            )
            .into(binding.moviePoster)

        binding.topAppBar.title = movie.localizedName
        binding.movieTitle.text = movie.localizedName

        val genres: String = movie.genres.joinToString("/")

        binding.movieAddInfo.text = if (genres.isNotEmpty()) {
            getString(R.string.movie_add_info_template, genres, movie.year)
        } else {
            getString(R.string.movie_year_template, movie.year)
        }

        binding.movieRatingValue.text = movie.rating.toString()
        binding.movieDescription.text = movie.description
    }

    private fun Int.dpToPx(context: Context): Int =
        (this * context.resources.displayMetrics.density).roundToInt()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}