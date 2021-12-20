package test.linkaja.testapp.favourite

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import junit.framework.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import test.linkaja.testapp.R
import test.linkaja.testapp.detailmovie.MovieDetailActivity
import test.linkaja.testapp.favourite.adapter.FavouriteMovieAdapter
import java.lang.Thread.sleep

@RunWith(JUnit4::class)
class FavouriteActivityTest{
    val ITEM_POS = 0
    @get: Rule
    var activityScenario = ActivityScenarioRule(FavouriteActivity::class.java)

    @Test fun test_visibility(){
        onView(withId(R.id.rvFavouriteMovie)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonBack)).check(matches(isDisplayed()))
    }


    @Test fun test_03_rv_click(){
        val am = getInstrumentation().addMonitor(MovieDetailActivity::class.java.name, null, true)
        sleep(2000)
        onView(withId(R.id.rvFavouriteMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FavouriteMovieAdapter.ListHolder>(
                ITEM_POS, click()
            )
        )

        am.waitForActivityWithTimeout(5000)
        assertEquals(1, am.hits)
    }

    @Test fun test_04_rv_item_click(){
        sleep(2000)
        onView(withId(R.id.rvFavouriteMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<FavouriteMovieAdapter.ListHolder>(
                ITEM_POS, click()
            )
        )

        sleep(2000)
        onView(withId(R.id.tvDetailTitle)).check(matches(ViewMatchers.withText("Fight Club")))
    }


}