package com.example.imovie.ui.tvshow.detail

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
import com.example.imovie.databinding.ActivityTvShowDetailBinding
import com.example.imovie.ui.movie.detail.MovieDetailActivity
import com.example.imovie.viewModel.TvShowDetailViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class TvShowDetailActivity : AppCompatActivity() {

    private val viewModel: TvShowDetailViewModel by viewModels()
    private lateinit var binding: ActivityTvShowDetailBinding
    private var statusFavorite: Boolean? = null
    private var favoriteData = FavoriteEntity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTvShowDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val tvShowId = intent?.getIntExtra(MovieDetailActivity.ID, 0)
        tvShowId?.let {
            getTvShow(it)
            checkFavorite(it)
        }


        binding.imgFavoriteTvShow.setOnClickListener {
            if (tvShowId != null) {
                if (statusFavorite == true) {
                    setUnfavorite(tvShowId)
                } else {
                    setFavorite(favoriteData)
                }
            }
        }
    }


    private fun getTvShow(id: Int) {
        viewModel.getTvShow(id).observe(this, { item ->
            if (item != null) {
                favoriteData = FavoriteEntity(
                    "TVSHOW",
                    item.name,
                    item.posterPath,
                    item.firstAirDate,
                    item.voteAverage,
                    item.id
                )

                binding.progressBar.visibility = View.GONE
                binding.tvDesTvshows.visibility = View.VISIBLE
                binding.tvRatingTvShows.visibility = View.VISIBLE
                binding.tvReleaseDateTvShow.visibility=View.VISIBLE
                binding.tvTvshows.visibility=View.VISIBLE
                binding.tvSynopsis.visibility=View.VISIBLE


                Glide.with(binding.root)
                    .load(BASE_IMAGE_URL + item.posterPath)
                    .into(binding.ivTvshows)



                binding.tvTvshows.text = item.name
                binding.tvDesTvshows.text = item.overview
                binding.tvRatingTvShows.text = item.voteAverage.toString()
                binding.tvReleaseDateTvShow.text = item.firstAirDate

            }
        })
    }


    private fun checkFavorite(id: Int) {
        viewModel.getFavoriteById(id).observe(this, {
            statusFavorite = it != null
            Log.d("TAG", "checkFavorite: $it")
            Log.d("TAG", "checkFavorite: $statusFavorite")
            if (statusFavorite == true) {
                binding.imgFavoriteTvShow.setImageDrawable(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.ic_favorite
                    )
                )
            } else {
                binding.imgFavoriteTvShow.setImageDrawable(
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
    companion object{
        const val ID = "ID"
    }
}