package com.example.imovie.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.imovie.data.entity.favorite.FavoriteEntity
import com.example.imovie.data.entity.movie.MovieEntity
import com.example.imovie.data.entity.tvshow.TvShowEntity
import com.example.imovie.data.local.DataDummy
import com.example.imovie.data.local.LocalDataSource
import com.example.imovie.data.remote.RemoteDataCallback
import com.example.imovie.data.remote.RemoteDataSource
import com.example.imovie.utils.LiveDataTestUtil
import com.example.imovie.utils.PagedListUtil
import com.nhaarman.mockitokotlin2.*
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

class AppRepositoryTest : TestCase() {

    @get:Rule
    var instantTaskExecuteRule = InstantTaskExecutorRule()

    private val remote = Mockito.mock(RemoteDataSource::class.java)
    private val local = Mockito.mock(LocalDataSource::class.java)

    private val repository = FakeAppRepository(remote, local)

    private val dummyFavorites = DataDummy.generateDummyFavorite()
    private val dummyFavorite = DataDummy.generateDummyFavorite()[0]
    private val favoriteId = dummyFavorite.id ?: 460465
    private val dummyMovies = DataDummy.generateDummyMovies()
    private val dummyMovie = DataDummy.generateDummyMovies()[0]
    private val movieId = dummyMovie.id
    private val dummyTvShows = DataDummy.generateDummyTvShows()
    private val dummyTvShow = DataDummy.generateDummyTvShows()[0]
    private val tvShowId = dummyTvShow.id

    @Test
    fun testInsertFavorite() {
        repository.insertFavorite(dummyFavorite)
        verify(local, times(1)).insertFavorite(dummyFavorite)
    }

    @Test
    fun testDeleteFavorite() {
        repository.deleteFavorite(favoriteId)
        verify(local, times(1)).deleteFavorite(favoriteId)
    }


    @Test
    fun testGetFavorite() {
        val dataSourceFactory =
            Mockito.mock(DataSource.Factory::class.java) as DataSource.Factory<Int, FavoriteEntity>
        Mockito.`when`(local.getFavorite()).thenReturn(dataSourceFactory)
        repository.getFavorite()

        val favorite = PagedListUtil.mockPagedList(DataDummy.generateDummyFavorite())
        verify(local).getFavorite()
        Assert.assertNotNull(favorite)
        assertEquals(dummyFavorites.size.toLong(), favorite.size.toLong())
    }

    @Test
    fun testGetFavoriteById() {
        val favorite = MutableLiveData<FavoriteEntity>()
        Mockito.`when`(favoriteId.let { local.getFavoriteById(it) }).thenReturn(favorite)
        favoriteId.let { repository.getFavoriteById(it) }

        val favoriteEntity = DataDummy.generateDummyFavorite()[0]
        favoriteId.let { verify(local).getFavoriteById(it) }
        Assert.assertNotNull(favoriteEntity)
        assertEquals(dummyFavorite, favoriteEntity)
    }

    @Test
    fun testGetPopularMovies() {
        CoroutineScope(Dispatchers.IO).launch {
            doAnswer { invocation ->
                (invocation.arguments[0] as RemoteDataCallback<List<MovieEntity>>)
                    .onDataReceived(dummyMovies)
                null
            }.`when`(remote).getPopularMovies(any())
            val movieEntities = LiveDataTestUtil.getValue(repository.getPopularMovies())
            verify(remote).getPopularMovies(any())
            Assert.assertNotNull(movieEntities)
            assertEquals(dummyMovies.size.toLong(), movieEntities.size.toLong())
        }

    }

    @Test
    fun testGetMovie() {
        CoroutineScope(Dispatchers.IO).launch {

            doAnswer { invocation ->
                (invocation.arguments[1] as RemoteDataCallback<MovieEntity>)
                    .onDataReceived(dummyMovie)
                null
            }.`when`(remote).getMovie(eq(movieId), any())
            val movieEntities = LiveDataTestUtil.getValue(repository.getMovie(movieId))
            verify(remote).getMovie(eq(movieId), any())
            Assert.assertNotNull(movieEntities)
            assertEquals(dummyMovie.id, movieEntities.id)
        }
    }

    @Test
    fun testGetPopularTvShow() {
        CoroutineScope(Dispatchers.IO).launch {
            doAnswer { invocation ->
                (invocation.arguments[0] as RemoteDataCallback<List<TvShowEntity>>)
                    .onDataReceived(dummyTvShows)
                null
            }.`when`(remote).getPopularTvShow(any())
            val tvShowEntities = LiveDataTestUtil.getValue(repository.getPopularTvShow())
            verify(remote).getPopularTvShow(any())
            Assert.assertNotNull(tvShowEntities)
            assertEquals(dummyTvShows.size.toLong(), dummyTvShows.size.toLong())
        }
    }

    @Test
    fun testGetTvShow() {
        CoroutineScope(Dispatchers.IO).launch {
            doAnswer { invocation ->
                (invocation.arguments[1] as RemoteDataCallback<TvShowEntity>)
                    .onDataReceived(dummyTvShow)
                null
            }.`when`(remote).getTvShow(eq(tvShowId), any())
            val tvShowEntity = LiveDataTestUtil.getValue(repository.getTvShow(tvShowId))
            verify(remote).getTvShow(eq(tvShowId), any())
            Assert.assertNotNull(tvShowEntity)
            assertEquals(dummyTvShow.id, tvShowEntity.id)
        }
    }
}