package test.linkaja.testapp

import org.junit.runner.RunWith
import org.junit.runners.Suite
import test.linkaja.testapp.detailmovie.MovieDetailActivityTest
import test.linkaja.testapp.favourite.FavouriteActivityTest
import test.linkaja.testapp.homescreen.HomeScreenActivity
import test.linkaja.testapp.homescreen.HomeScreenActivityTest

@RunWith(Suite::class)
@Suite.SuiteClasses(
    HomeScreenActivityTest::class, FavouriteActivityTest::class, MovieDetailActivityTest::class
)
class SuitTest