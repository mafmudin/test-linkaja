package test.linkaja.testapp.detailmovie.repository

import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.api.ApiServices
import test.linkaja.testapp.detailmovie.model.MovieDetailResponse
import test.linkaja.testapp.model.Resource
import test.linkaja.testapp.util.LanguageHelper
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class DetailMovieRepository @Inject constructor(
    private val apiServices: ApiServices
) {
    suspend fun getMovieDetail(id: String): Resource<MovieDetailResponse> {
        return try {
            val response = apiServices.detailMovie(
                id,
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