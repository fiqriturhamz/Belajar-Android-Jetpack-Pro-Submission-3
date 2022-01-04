package com.example.imovie


import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import com.example.imovie.activity.MainActivity
import com.example.imovie.data.local.DataDummy
import com.example.imovie.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test


class MainActivityTest {

    private val dummyMovies = DataDummy.generateDummyMovies()
    private val dummyTvShows = DataDummy.generateDummyTvShows()
    private val dummyFavorite = DataDummy.generateDummyFavorite()

    @Before
    fun setUp() {
        ActivityScenario.launch(MainActivity::class.java)
        IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResouce)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResouce)
    }

    @Test
    fun loadMovies() {
        onView(withId(R.id.rv_movie))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dummyMovies.size))
    }

    @Test
    fun loadDetailMovie() {
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        onView(withId(R.id.tv_movie)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_release_date)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_rating)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_des)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_poster)).check(matches(isDisplayed()))
        onView(withId(R.id.img_favorite)).check(matches(isDisplayed()))


    }

    @Test
    fun getFavoriteMovies() {
        onView(withId(R.id.rv_movie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                3,
                ViewActions.click()
            )
        )
        onView(withId(R.id.img_favorite)).perform(ViewActions.click())
    }


    @Test
    fun loadFavoritesMovies() {
        onView(withId(R.id.action_favorite)).perform(ViewActions.click())

        onView(withId(R.id.rv_movie))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_movie))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dummyFavorite.size))


    }

    @Test
    fun loadTvShows() {

        onView(withText("Tv Show")).perform(ViewActions.click())
        onView(withId(R.id.rv_tv_show))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_tv_show))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dummyTvShows.size))
    }

    @Test
    fun loadDetailTvShow() {
        onView(withText("Tv Show")).perform(ViewActions.click())
        onView(withId(R.id.rv_tv_show)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )

        onView(withId(R.id.tv_tvshows)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_release_date_tv_show)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_rating_tv_shows)).check(matches(isDisplayed()))
        onView(withId(R.id.tv_des_tvshows)).check(matches(isDisplayed()))
        onView(withId(R.id.iv_tvshows)).check(matches(isDisplayed()))
        onView(withId(R.id.img_favorite_tv_show)).check(matches(isDisplayed()))

    }

    @Test
    fun getFavoriteTvShow() {
        onView(withText("Tv Show")).perform(ViewActions.click())
        onView(withId(R.id.rv_tv_show)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                2,
                ViewActions.click()
            )
        )
        onView(withId(R.id.img_favorite_tv_show)).perform(ViewActions.click())
    }

    @Test
    fun loadFavoritesTvShow() {
        onView(withId(R.id.action_favorite)).perform(ViewActions.click())
        onView(withText("TV SHOW")).perform(ViewActions.click())

        onView(withId(R.id.rv_tv_show))
            .check(matches(isDisplayed()))
        onView(withId(R.id.rv_tv_show))
            .perform(RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(dummyFavorite.size))


    }

}
