package com.example.imovie.ui.movie.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.imovie.BuildConfig.BASE_IMAGE_URL
import com.example.imovie.R
import com.example.imovie.data.entity.favorite.FavoriteEntity
import com.example.imovie.databinding.ActivityMovieDetailBinding
import com.example.imovie.viewModel.MovieDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private val viewModel: MovieDetailViewModel by viewModels()
    private lateinit var binding: ActivityMovieDetailBinding
    private var statusFavorite: Boolean? = null
    private var favoriteData = FavoriteEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val movieID = intent?.getIntExtra(ID, 0)
        movieID?.let {
            getMovie(it)
            checkFavorite(it)
        }


        binding.imgFavorite.setOnClickListener {
            if (movieID != null) {
                if (statusFavorite == true) {
                    setUnfavorite(movieID)
                } else {
                    setFavorite(favoriteData)
                }
            }
        }
    }

    private fun getMovie(id: Int) {
        viewModel.getMovie(id).observe(this, { item ->
            if (item != null) {
                favoriteData = FavoriteEntity(
                    "MOVIE",
                    item.title,
                    item.posterPath,
                    item.releaseDate,
                    item.voteAverage,
                    item.id
                )

                binding.progressBar.visibility = View.GONE
                binding.tvDes.visibility = View.VISIBLE
                binding.tvRating.visibility = View.VISIBLE
                binding.tvMovie.visibility=View.VISIBLE
                binding.tvSynopsis.visibility=View.VISIBLE
                binding.tvReleaseDate.visibility=View.VISIBLE


                Glide.with(binding.root)
                    .load(BASE_IMAGE_URL + item.posterPath)
                    .into(binding.ivPoster)


                binding.tvMovie.text = item.title
                binding.tvDes.text = item.overview
                binding.tvRating.text = item.voteAverage.toString()
                binding.tvReleaseDate.text = item.releaseDate

            }
        })
    }

    private fun checkFavorite(id: Int) {
        viewModel.getFavoriteById(id).observe(this, {
            statusFavorite = it != null
            Log.d("TAG", "checkFavorite: $it")
            Log.d("TAG", "checkFavorite: $statusFavorite")
            if (statusFavorite == true) {
                binding.imgFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_favorite
                    )
                )
            } else {
                binding.imgFavorite.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_favorite_border
                    )
                )
            }
        })

    }

    private fun setFavorite(favoriteEntity: FavoriteEntity) {
        viewModel.insertFavorite(favoriteEntity)
        statusFavorite = true
        favoriteEntity.id?.let { checkFavorite(it) }
    }

    private fun setUnfavorite(id: Int) {
        viewModel.deleteFavorite(id)
        statusFavorite = false
        checkFavorite(id)
    }
    companion object {
        const val ID = "ID"
    }
}
