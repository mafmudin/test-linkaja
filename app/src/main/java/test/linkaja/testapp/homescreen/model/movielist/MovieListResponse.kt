package test.linkaja.testapp.homescreen.model.movielist


import com.google.gson.annotations.SerializedName

data class MovieListResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("page")
    val page: Int?,
    @SerializedName("results")
    val movieItems: List<MovieItem>?,
    @SerializedName("total_pages")
    val totalPages: Int?,
    @SerializedName("total_results")
    val totalResults: Int?
)