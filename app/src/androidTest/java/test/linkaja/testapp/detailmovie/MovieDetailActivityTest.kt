package test.linkaja.testapp.detailmovie

import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import test.linkaja.testapp.R
import test.linkaja.testapp.homescreen.HomeScreenActivity
import test.linkaja.testapp.homescreen.adapter.MovieAdapter

@RunWith(JUnit4::class)
class MovieDetailActivityTest{
    val ITEM_POS = 0
    @get: Rule
    var activityScenario = ActivityScenarioRule(HomeScreenActivity::class.java)

    @Test fun test_visibility(){
        Espresso.onView(ViewMatchers.withId(R.id.rvMovie)).perform(
            RecyclerViewActions.actionOnItemAtPosition<MovieAdapter.ListHolder>(
                ITEM_POS, ViewActions.click()
            )
        )

        onView(withId(R.id.ivDetailImage)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonBack)).check(matches(isDisplayed()))
        onView(withId(R.id.tvDetailTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.tvRuntime)).check(matches(isDisplayed()))
        onView(withId(R.id.tvReleaseDate)).check(matches(isDisplayed()))
        onView(withId(R.id.tvRating)).check(matches(isDisplayed()))
        onView(withId(R.id.buttonFavorite)).check(matches(isDisplayed()))
        onView(withId(R.id.rvGenre)).check(matches(isDisplayed()))
        onView(withId(R.id.tvOverview)).check(matches(isDisplayed()))
    }
}