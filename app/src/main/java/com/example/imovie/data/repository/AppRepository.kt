package com.example.imovie.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.imovie.data.entity.favorite.FavoriteEntity
import com.example.imovie.data.entity.movie.MovieEntity
import com.example.imovie.data.entity.tvshow.TvShowEntity
import com.example.imovie.data.local.LocalDataSource
import com.example.imovie.data.remote.RemoteDataCallback
import com.example.imovie.data.remote.RemoteDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) {

    fun getFavorite(): LiveData<PagedList<FavoriteEntity>> = LivePagedListBuilder(localDataSource.getFavorite(), 5).build()

    fun getFavoriteById(id: Int) = localDataSource.getFavoriteById(id)

    fun insertFavorite(favorite: FavoriteEntity) = localDataSource.insertFavorite(favorite)

    fun deleteFavorite(id: Int) = localDataSource.deleteFavorite(id)

    fun getPopularMovies(): LiveData<List<MovieEntity>> {
        val movieResult = MutableLiveData<List<MovieEntity>>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getPopularMovies(object : RemoteDataCallback<List<MovieEntity>> {
                override fun onDataReceived(response: List<MovieEntity>) {
                    val movieList = ArrayList<MovieEntity>()
                    for (data in response) {
                        val movie = MovieEntity(
                            data.overview,
                            data.title,
                            data.posterPath,
                            data.releaseDate,
                            data.voteAverage,
                            data.id
                        )
                        movieList.add(movie)
                    }
                    movieResult.postValue(movieList.take(10))
                }
            })
        }
        return movieResult
    }

    fun getMovie(id: Int?): LiveData<MovieEntity> {
        val movieResult = MutableLiveData<MovieEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getMovie(id, object : RemoteDataCallback<MovieEntity> {
                override fun onDataReceived(response: MovieEntity) {
                    val movie = MovieEntity(
                        response.overview,
                        response.title,
                        response.posterPath,
                        response.releaseDate,
                        response.voteAverage,
                        response.id
                    )
                    movieResult.postValue(movie)
                }
            })
        }
        return movieResult
    }

    fun getPopularTvShow(): LiveData<List<TvShowEntity>> {
        val tvShowResult = MutableLiveData<List<TvShowEntity>>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getPopularTvShow(object : RemoteDataCallback<List<TvShowEntity>> {
                override fun onDataReceived(response: List<TvShowEntity>) {
                    val tvShowist = ArrayList<TvShowEntity>()
                    for (data in response) {
                        val tvShow = TvShowEntity(
                            data.overview,
                            data.name,
                            data.posterPath,
                            data.firstAirDate,
                            data.voteAverage,
                            data.id
                        )
                        tvShowist.add(tvShow)
                    }
                    tvShowResult.postValue(tvShowist.take(10))
                }
            })
        }
        return tvShowResult
    }

    fun getTvShow(id: Int?): LiveData<TvShowEntity> {
        val tvShowResult = MutableLiveData<TvShowEntity>()
        CoroutineScope(Dispatchers.IO).launch {
            remoteDataSource.getTvShow(id, object : RemoteDataCallback<TvShowEntity> {
                override fun onDataReceived(response: TvShowEntity) {
                    val tvShow = TvShowEntity(
                        response.overview,
                        response.name,
                        response.posterPath,
                        response.firstAirDate,
                        response.voteAverage,
                        response.id
                    )
                    tvShowResult.postValue(tvShow)
                }
            })
        }
        return tvShowResult
    }
}