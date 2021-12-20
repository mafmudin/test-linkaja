package test.linkaja.testapp.homescreen

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import junit.framework.Assert.assertEquals
import org.hamcrest.CoreMatchers.not
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import test.linkaja.testapp.R
import test.linkaja.testapp.detailmovie.MovieDetailActivity
import test.linkaja.testapp.favourite.FavouriteActivity
import test.linkaja.testapp.homescreen.adapter.MovieAdapter
import java.lang.Thread.sleep

@RunWith(JUnit4::class)
class HomeScreenActivityTest{

    val ITEM_POS = 0
    @get: Rule
    var activityScenario = ActivityScenarioRule(HomeScreenActivity::class.java)

    @Test fun test_01_visibility(){
        onView(withId(R.id.buttonFavorite)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonSearch)).check(matches(isDisplayed()))
        onView(withId(R.id.viewPager)).check(matches(isDisplayed()))
        onView(withId(R.id.rvGenre)).check(matches(isDisplayed()))
        onView(withId(R.id.rvMovie)).check(matches(isDisplayed()))
    }

    @Test fun test_02_button_search(){
        sleep(2000)
        onView(withId(R.id.buttonSearch)).perform(click())
        sleep(1000)
        onView(withId(R.id.llSearchName)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonFavorite)).check(matches(not(isDisplayed())))
        onView(withId(R.id.viewPager)).check(matches(not(isDisplayed())))
    }

    @Test fun test_03_close_search(){
        onView(withId(R.id.buttonSearch)).perform(click())
        sleep(1000)

        onView(withId(R.id.llSearchName)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonCloseSearch)).check(matches(isDisplayed()))

        onView(withId(R.id.buttonCloseSearch)).perform(click())
        sleep(1000)

        onView(withId(R.id.buttonFavorite)).check(matches(isDisplayed()))
        onView(withId(R.id.viewPager)).check(matches(isDisplayed()))
    }

    @Test fun test_04_test_intent(){
        sleep(2000)
        val am = getInstrumentation().addMonitor(MovieDetailActivity::class.java.name, null, true)
        onView(withId(R.id.rvMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MovieAdapter.ListHolder>(
                ITEM_POS, click()
            )
        )

        am.waitForActivityWithTimeout(2000)
        assertEquals(1, am.hits)
    }

    //check title as it first index
    @Test fun test_05_select_movie(){
        sleep(2000)
        onView(withId(R.id.rvMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MovieAdapter.ListHolder>(
                ITEM_POS, click()
            )
        )
        sleep(5000)
        onView(withId(R.id.tvDetailTitle)).check(matches(withText("Spider-Man")))
    }

    @Test fun test_06_intent_favourite(){
        val am = getInstrumentation().addMonitor(FavouriteActivity::class.java.name, null, true)
        onView(withId(R.id.buttonFavorite)).perform(click())
        am.waitForActivityWithTimeout(2000)
        assertEquals(1, am.hits)
    }
}