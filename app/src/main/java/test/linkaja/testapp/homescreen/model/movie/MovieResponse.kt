package test.linkaja.testapp.homescreen.model.movie

import com.google.gson.annotations.SerializedName
import test.linkaja.testapp.homescreen.model.movie.Movie

data class MovieResponse(
    val page: Int,
    @SerializedName("results")
    val movies: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)