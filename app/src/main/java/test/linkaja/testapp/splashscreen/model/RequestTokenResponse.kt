package test.linkaja.testapp.splashscreen.model

data class RequestTokenResponse(
    val expires_at: String,
    val request_token: String,
    val success: Boolean
)