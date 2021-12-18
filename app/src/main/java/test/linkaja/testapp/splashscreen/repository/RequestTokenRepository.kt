package test.linkaja.testapp.splashscreen.repository

import retrofit2.Response
import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.api.ApiServices
import test.linkaja.testapp.model.Resource
import test.linkaja.testapp.splashscreen.model.RequestTokenResponse
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class RequestTokenRepository @Inject constructor(private val apiServices: ApiServices) {
    suspend fun requestToken(): Resource<RequestTokenResponse>{
        return try {
            val response = apiServices.requestToken(BuildConfig.API_KEY)
            val result = response.body()
            if (response.isSuccessful && result != null){
                Resource.Success(result)
            }else{
                Resource.Error("An error occurred")
            }
        }catch (e:Exception){
            Timber.e("Error %s", e)
            Resource.Error("An $e occured")
        }
    }
}