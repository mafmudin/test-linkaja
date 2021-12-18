package test.linkaja.testapp.api

import retrofit2.Response
import retrofit2.http.*
import test.linkaja.testapp.model.Resource
import test.linkaja.testapp.splashscreen.model.CreateSessionResponse
import test.linkaja.testapp.splashscreen.model.RequestTokenResponse

interface ApiServices {
    @GET("authentication/token/new")
    suspend fun requestToken(
        @Query("api_key") apiKey:String
    ): Response<RequestTokenResponse>

    @FormUrlEncoded
    @POST("/authentication/session/new")
    suspend fun createSession(
        @Query("api_key") apiKey:String,
        @Field("request_token") requestToken:String
    ): Response<CreateSessionResponse>
}