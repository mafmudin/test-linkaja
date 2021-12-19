package test.linkaja.testapp.favourite.model


import com.google.gson.annotations.SerializedName

data class SetFavouriteResponse(
    @SerializedName("status_code")
    val statusCode: Int?,
    @SerializedName("status_message")
    val statusMessage: String?,
    @SerializedName("success")
    val success: Boolean?
)