package test.linkaja.testapp.homescreen.model.movie

import test.linkaja.testapp.homescreen.model.movie.Movie

data class MovieResponse(
    val page: Int,
    val movies: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)