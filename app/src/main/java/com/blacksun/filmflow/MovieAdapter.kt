package com.blacksun.filmflow

import android.content.Context
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blacksun.filmflow.data.Movie
import com.blacksun.filmflow.databinding.ItemMovieBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import java.util.Locale
import kotlin.math.roundToInt

class MovieAdapter(
    private val allMovies: List<Movie>,
    private val onMovieClicked: (Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieItemViewHolder>() {

    companion object {
        private const val CORNER_RADIUS_DP = 8
    }

    private var filteredMovies = allMovies.toMutableList()
    private var selectedGenre: String? = null

    fun setGenreFilter(genre: String?) {
        selectedGenre = genre?.normalizeGenre()
        filterMovies()
        notifyDataSetChanged()
    }

    private fun filterMovies() {
        filteredMovies = if (selectedGenre == null) {
            allMovies.toMutableList()
        } else {
            allMovies.filter { movie ->
                movie.genres.any { genre ->
                    genre.normalizeGenre() == selectedGenre
                }
            }.toMutableList()
        }
    }

    private fun String.normalizeGenre(): String {
        return this.trim()
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.getDefault())
                else it.toString()
            }
    }

    inner class MovieItemViewHolder(private val binding: ItemMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            with(binding) {
                Glide.with(moviePoster.context)
                    .load(movie.imageUrl)
                    .transform(
                        MultiTransformation(
                            RoundedCorners(CORNER_RADIUS_DP.dpToPx(moviePoster.context)),
                            CenterCrop()
                        )
                    )
                    .placeholder(R.drawable.movie_img_placeholder)
                    .error(R.drawable.movie_img_placeholder)
                    .into(moviePoster)

                movieTitle.text = movie.localizedName
                root.setOnClickListener { onMovieClicked(movie) }
            }
        }

        private fun Int.dpToPx(context: Context): Int =
            (this * context.resources.displayMetrics.density).roundToInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MovieItemViewHolder(
            ItemMovieBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MovieItemViewHolder, position: Int) {
        holder.bind(filteredMovies[position])
    }

    override fun getItemCount() = filteredMovies.size
}