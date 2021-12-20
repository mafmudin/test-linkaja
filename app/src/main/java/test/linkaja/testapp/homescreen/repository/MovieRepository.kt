package test.linkaja.testapp.homescreen.repository

import test.linkaja.testapp.BuildConfig
import test.linkaja.testapp.api.ApiServices
import test.linkaja.testapp.database.GenreDao
import test.linkaja.testapp.database.MovieDao
import test.linkaja.testapp.homescreen.model.genres.Genre
import test.linkaja.testapp.homescreen.model.genres.GenresResponse
import test.linkaja.testapp.homescreen.model.movie.Movie
import test.linkaja.testapp.homescreen.model.movie.MovieResponse
import test.linkaja.testapp.homescreen.model.movielist.MovieListResponse
import test.linkaja.testapp.model.Resource
import test.linkaja.testapp.util.LanguageHelper
import java.lang.Exception
import java.util.*
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val apiServices: ApiServices,
    private val genreDao: GenreDao,
    private val movieDao: MovieDao
) {
    fun insertAllGenre(genres: List<Genre>) = genreDao.insertAll(genres)
    fun getAllGenres() = genreDao.getAllGenre()

    fun insertAllMovie(movies: MutableList<Movie>) = movieDao.insertAll(movies)
    fun getAllMovie() = movieDao.getAllMovie()

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
                insertAllGenre(result.genres)
                Resource.Success(result)
            } else {
                Resource.Error("An Error occurred")
            }
        } catch (e: Exception) {
            Resource.Error("An $e occurred")
        }

    }

    suspend fun getMovieByGenre(id: String, page: Int): Resource<MovieResponse> {
        return try {
            val response = apiServices.getMovieByGenre(
                id,
                BuildConfig.API_KEY,
                LanguageHelper.getLangByLocale(
                    Locale.getDefault()
                ),
                page
            )

            val result = response.body()
            if (response.isSuccessful && result != null) {
                insertAllMovie(result.movies)
                Resource.Success(result)
            } else {
                Resource.Error("An Error occurred")
            }
        } catch (e: Exception) {
            Resource.Error("An $e occurred")
        }

    }

    suspend fun getMovie(page: Int): Resource<MovieResponse> {
        return try {
            val response = apiServices.getMovie(
                "popularity.desc",
                BuildConfig.API_KEY,
                LanguageHelper.getLangByLocale(
                    Locale.getDefault()
                ),
                page
            )

            val result = response.body()
            if (response.isSuccessful && result != null) {
                insertAllMovie(result.movies)
                Resource.Success(result)
            } else {
                Resource.Error("An Error occurred")
            }
        } catch (e: Exception) {
            Resource.Error("An $e occurred")
        }

    }

    suspend fun getHighestRateMovie(): Resource<MovieResponse> {
        return try {
            val response = apiServices.getHighestRateMovie(
                "vote_average.desc",
                "R",
                BuildConfig.API_KEY
            )

            val result = response.body()
            if (response.isSuccessful && result != null) {
                insertAllMovie(result.movies)
                Resource.Success(result)
            } else {
                Resource.Error("An Error occurred")
            }
        } catch (e: Exception) {
            Resource.Error("An $e occurred")
        }

    }

    suspend fun searchMovie(query: String, page: Int): Resource<MovieResponse> {
        return try {
            val response = apiServices.searchMovie(
                query,
                page,
                BuildConfig.API_KEY
            )

            val result = response.body()
            if (response.isSuccessful && result != null) {
                insertAllMovie(result.movies)
                Resource.Success(result)
            } else {
                Resource.Error("An Error occurred")
            }
        } catch (e: Exception) {
            Resource.Error("An $e occurred")
        }

    }
}