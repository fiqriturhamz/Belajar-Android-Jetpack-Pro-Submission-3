package com.example.imovie.ui.favorite.movie

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import com.example.imovie.data.entity.favorite.FavoriteEntity
import com.example.imovie.data.local.DataDummy
import com.example.imovie.data.repository.AppRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.util.concurrent.Executors


@RunWith(MockitoJUnitRunner::class)
class FavoriteMovieViewModelTest {
    private lateinit var viewModel: FavoriteMovieViewModel

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: AppRepository

    @Mock
    private lateinit var observer: Observer<PagedList<FavoriteEntity>>

    @Before
    fun setUp() {
        viewModel = FavoriteMovieViewModel(repository)
    }


    @Test
    fun testGetFavoriteMovie() {
        val expected = MutableLiveData<PagedList<FavoriteEntity>>()
        expected.value = PagedTestDataSources.snapshot(DataDummy.generateDummyFavorite())

        Mockito.`when`(repository.getFavorite()).thenReturn(expected)

        viewModel.getFavoriteMovie().observeForever(observer)
        Mockito.verify(observer).onChanged(expected.value)

        val expectedValue = expected.value
        val actualValue = viewModel.getFavoriteMovie().value
        Assert.assertEquals(expectedValue, actualValue)
        Assert.assertEquals(expectedValue?.snapshot(), actualValue?.snapshot())
        Assert.assertEquals(expectedValue?.size, actualValue?.size)
    }

    @Test
    fun testGetFavoriteMovieEmpty() {
        val expected = MutableLiveData<PagedList<FavoriteEntity>>()
        expected.value = PagedTestDataSources.snapshot()

        Mockito.`when`(repository.getFavorite()).thenReturn(expected)

        viewModel.getFavoriteMovie().observeForever(observer)
        Mockito.verify(observer).onChanged(expected.value)

        val actualValueDataSize = viewModel.getFavoriteMovie().value?.size
        Assert.assertTrue(
            "size of data should be 0, actual is $actualValueDataSize",
            actualValueDataSize == 0
        )
    }

    class PagedTestDataSources private constructor(private val items: List<FavoriteEntity>) :
        PositionalDataSource<FavoriteEntity>() {
        companion object {
            fun snapshot(items: List<FavoriteEntity> = listOf()): PagedList<FavoriteEntity> {
                return PagedList.Builder(PagedTestDataSources(items), 5)
                    .setNotifyExecutor(Executors.newSingleThreadExecutor())
                    .setFetchExecutor(Executors.newSingleThreadExecutor())
                    .build()
            }
        }

        override fun loadInitial(
            params: LoadInitialParams,
            callback: LoadInitialCallback<FavoriteEntity>
        ) {
            callback.onResult(items, 0, items.size)
        }

        override fun loadRange(
            params: LoadRangeParams,
            callback: LoadRangeCallback<FavoriteEntity>
        ) {
            val start = params.startPosition
            val end = params.startPosition + params.loadSize
            callback.onResult(items.subList(start, end))
        }
    }
}