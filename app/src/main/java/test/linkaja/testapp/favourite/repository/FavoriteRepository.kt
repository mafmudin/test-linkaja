package test.linkaja.testapp.favourite.repository

import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.api.ApiServices
import test.linkaja.testapp.detailmovie.model.MovieDetailResponse
import test.linkaja.testapp.homescreen.model.movie.MovieResponse
import test.linkaja.testapp.model.Resource
import test.linkaja.testapp.util.LanguageHelper
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class FavoriteRepository @Inject constructor(
    private val apiServices: ApiServices) {
    suspend fun getFavourite(): Resource<MovieResponse> {
        return try {
            val response = apiServices.getFavourite(
                BuildConfig.ACCOUNT_ID,
                BuildConfig.API_KEY,
                BuildConfig.SESSION_ID
            )

            val result = response.body()
            if (response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error("An Error occurred")
            }
        } catch (e: Exception) {
            Resource.Error("An $e occurred")
        }
    }
}