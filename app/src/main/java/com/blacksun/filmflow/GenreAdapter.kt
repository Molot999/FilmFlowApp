package com.blacksun.filmflow

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.blacksun.filmflow.data.MovieGenreDisplay
import com.blacksun.filmflow.databinding.ItemGenreBinding

class GenreAdapter(
    private val movieGenreDisplayList: List<MovieGenreDisplay>,
    private val onGenreSelected: (MovieGenreDisplay?) -> Unit
) : RecyclerView.Adapter<GenreAdapter.GenreItemViewHolder>() {

    private var selectedMovieGenreDisplay: MovieGenreDisplay? = null

    private fun setSelectedGenre(genre: MovieGenreDisplay?) {
        val prevSelected = selectedMovieGenreDisplay
        selectedMovieGenreDisplay = if (prevSelected == genre) null else genre
        notifyItemChanged(movieGenreDisplayList.indexOf(prevSelected))
        notifyItemChanged(movieGenreDisplayList.indexOf(selectedMovieGenreDisplay))
        onGenreSelected(selectedMovieGenreDisplay)
    }

    inner class GenreItemViewHolder(private val binding: ItemGenreBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(genre: MovieGenreDisplay) {
            binding.genreTitle.text = genre.displayName

            val bgColor = if (genre == selectedMovieGenreDisplay) {
                R.color.accent_yellow
            } else {
                Color.TRANSPARENT
            }
            binding.root.setBackgroundResource(bgColor)

            binding.root.setOnClickListener {
                setSelectedGenre(genre)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreItemViewHolder {
        val binding = ItemGenreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GenreItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GenreItemViewHolder, position: Int) {
        holder.bind(movieGenreDisplayList[position])
    }

    override fun getItemId(position: Int): Long {
        return movieGenreDisplayList[position].hashCode().toLong()
    }

    override fun getItemCount(): Int = movieGenreDisplayList.size
}