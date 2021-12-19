package test.linkaja.testapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import test.linkaja.testapp.homescreen.model.genres.Genre
import test.linkaja.testapp.homescreen.model.genres.GenresResponse
import test.linkaja.testapp.homescreen.model.movie.MovieResponse
import test.linkaja.testapp.homescreen.model.movielist.MovieListResponse

interface ApiServices {
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Response<GenresResponse>

    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("with_genres") genre: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("discover/movie")
    suspend fun getMovie(
        @Query("sort_by") shortBy: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: Int
    ): Response<MovieResponse>

    @GET("discover/movie")
    suspend fun getHighestRateMovie(
        @Query("sort_by") shortBy: String,
        @Query("certification") certification: String,
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>
}