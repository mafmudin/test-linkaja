package test.linkaja.testapp.homescreen.repository

import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.api.ApiServices
import test.linkaja.testapp.homescreen.model.genres.GenresResponse
import test.linkaja.testapp.model.Resource
import test.linkaja.testapp.util.LanguageHelper
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class GenreRepository @Inject constructor(
    private val apiServices: ApiServices
) {

    suspend fun getGenres(): Resource<GenresResponse> {
        return try {
            val response = apiServices.getGenres(
                BuildConfig.API_KEY,
                LanguageHelper.getLangByLocale(
                    Locale.getDefault()
                )
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