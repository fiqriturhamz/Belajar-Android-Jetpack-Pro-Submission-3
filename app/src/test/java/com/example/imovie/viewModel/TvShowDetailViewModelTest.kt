package com.example.imovie.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.imovie.data.entity.favorite.FavoriteEntity
import com.example.imovie.data.entity.tvshow.TvShowEntity
import com.example.imovie.data.local.DataDummy
import com.example.imovie.data.repository.AppRepository
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class TvShowDetailViewModelTest {
    private lateinit var viewModel: TvShowDetailViewModel
    private val dummyTvShow = DataDummy.generateDummyTvShows()[0]
    private val tvShowId = dummyTvShow.id
    private val dummyFavorite = DataDummy.generateDummyFavorite()[0]
    private val favoriteId = dummyFavorite.id ?: 460465

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: AppRepository

    @Mock
    private lateinit var observer: Observer<TvShowEntity>

    @Mock
    private lateinit var observerFavorite: Observer<FavoriteEntity>

    @Before
    fun setUp() {
        viewModel = TvShowDetailViewModel(repository)
    }

    @Test
    fun testGetTvShow() {
        val tvShow = MutableLiveData<TvShowEntity>()
        tvShow.value = dummyTvShow

        Mockito.`when`(repository.getTvShow(tvShowId)).thenReturn(tvShow)
        val tvShowEntity = tvShowId?.let { viewModel.getTvShow(it).value } as TvShowEntity
        verify(repository).getTvShow(tvShowId)

        Assert.assertNotNull(tvShowEntity)
        Assert.assertEquals(dummyTvShow.id, tvShowEntity.id)
        Assert.assertEquals(dummyTvShow.name, tvShowEntity.name)
        Assert.assertEquals(dummyTvShow.voteAverage, tvShowEntity.voteAverage, 0.0)
        Assert.assertEquals(dummyTvShow.firstAirDate, tvShowEntity.firstAirDate)
        Assert.assertEquals(dummyTvShow.overview, tvShowEntity.overview)

        viewModel.getTvShow(tvShowId).observeForever(observer)
        verify(observer).onChanged(dummyTvShow)
    }

    @Test
    fun testGetTvShowFavoriteById() {
        val favorite = MutableLiveData<FavoriteEntity>()
        favorite.value = dummyFavorite

        Mockito.`when`(favoriteId.let { repository.getFavoriteById(it) }).thenReturn(favorite)
        val favoriteEntity =
            favoriteId.let { viewModel.getFavoriteById(it).value } as FavoriteEntity
        verify(repository).getFavoriteById(favoriteId)

        Assert.assertNotNull(favoriteEntity)
        Assert.assertEquals(dummyFavorite.id, favoriteEntity.id)
        Assert.assertEquals(dummyFavorite.title, favoriteEntity.title)
        Assert.assertEquals(dummyFavorite.category, favoriteEntity.category)
        Assert.assertEquals(dummyFavorite.posterPath, favoriteEntity.posterPath)
        Assert.assertEquals(dummyFavorite.voteAverage, favoriteEntity.voteAverage, 0.0)
        Assert.assertEquals(dummyFavorite.releaseDate, favoriteEntity.releaseDate)

        viewModel.getFavoriteById(favoriteId).observeForever(observerFavorite)
        verify(observerFavorite).onChanged(dummyFavorite)
    }

    @Test
    fun testInsertTvShowFavorite() {
        viewModel.insertFavorite(dummyFavorite)
        verify(repository, times(1)).insertFavorite(dummyFavorite)
    }

    @Test
    fun testDeleteTvShowFavorite() {
        viewModel.deleteFavorite(favoriteId)
        verify(repository, times(1)).deleteFavorite(favoriteId)
    }
}