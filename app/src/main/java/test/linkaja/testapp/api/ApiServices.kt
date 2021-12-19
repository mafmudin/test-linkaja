package test.linkaja.testapp.api

import retrofit2.Response
import retrofit2.http.*
import test.linkaja.testapp.detailmovie.model.MovieDetailResponse
import test.linkaja.testapp.favourite.model.SetFavouriteResponse
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

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Response<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun detailMovie(
        @Path(value = "movie_id", encoded = true) movieId: String,
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Response<MovieDetailResponse>

    @GET("account/{account_id}/favorite/movies")
    suspend fun getFavourite(
        @Path(value = "account_id", encoded = true) accountId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") session: String
    ): Response<MovieResponse>

    @FormUrlEncoded
    @POST("account/{account_id}/favorite")
    suspend fun setFavourite(
        @Path(value = "account_id", encoded = true) accountId: String,
        @Query("api_key") apiKey: String,
        @Query("session_id") session: String,
        @Field("media_type") mediaType: String,
        @Field("media_id") mediaId: Int,
        @Field("favorite") favorite: Boolean
    ): Response<SetFavouriteResponse>
}