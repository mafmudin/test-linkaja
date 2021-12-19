package test.linkaja.testapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import test.linkaja.testapp.homescreen.model.genres.Genre
import test.linkaja.testapp.homescreen.model.genres.GenresResponse

interface ApiServices {
    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("api_key") apiKey: String,
        @Query("language") language: String
    ): Response<GenresResponse>
}