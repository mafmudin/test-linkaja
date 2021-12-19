package test.linkaja.testapp.homescreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import test.linkaja.testapp.homescreen.model.genres.Genre
import test.linkaja.testapp.homescreen.model.movie.Movie
import test.linkaja.testapp.homescreen.model.movielist.MovieItem
import test.linkaja.testapp.homescreen.repository.GenreRepository
import test.linkaja.testapp.model.Resource
import javax.inject.Inject

@HiltViewModel
class GenresViewModel @Inject constructor(private val genreRepository: GenreRepository):ViewModel() {
    sealed class GenreEvent{
        class Success(val genresResponse: List<Genre>): GenreEvent()
        class Error(val errorMessage: String): GenreEvent()
        object Loading: GenreEvent()
        object Empty: GenreEvent()
    }

    sealed class MovieEvent{
        class Success(val movieItem: List<Movie>): MovieEvent()
        class Error(val errorMessage: String): MovieEvent()
        object Loading: MovieEvent()
        object Empty: MovieEvent()
    }

    sealed class PopularMovieEvent{
        class Success(val movieItem: List<Movie>): PopularMovieEvent()
        class Error(val errorMessage: String): PopularMovieEvent()
        object Loading: PopularMovieEvent()
        object Empty: PopularMovieEvent()
    }

    sealed class HighestRateMovieEvent{
        class Success(val movieItem: List<Movie>): HighestRateMovieEvent()
        class Error(val errorMessage: String): HighestRateMovieEvent()
        object Loading: HighestRateMovieEvent()
        object Empty: HighestRateMovieEvent()
    }

    private val _conversion = MutableStateFlow<GenreEvent>(GenreEvent.Empty)
    val conversion : StateFlow<GenreEvent> = _conversion

    fun getGenres(){
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = GenreEvent.Loading
            when(val response = genreRepository.getGenres()){
                is Resource.Error -> _conversion.value = GenreEvent.Error(response.message!!)
                is Resource.Success -> {
                    _conversion.value = GenreEvent.Success(response.data!!.genres)
                }
            }
        }
    }

    private val _conversionMovie = MutableStateFlow<MovieEvent>(MovieEvent.Empty)
    val conversionMovie : StateFlow<MovieEvent> = _conversionMovie

    fun getMovieByGenre(id: String, page: Int){
        viewModelScope.launch(Dispatchers.IO){
            _conversionMovie.value = MovieEvent.Loading
            when(val response = genreRepository.getMovieByGenre(id, page)){
                is Resource.Error -> _conversionMovie.value = MovieEvent.Error(response.message!!)
                is Resource.Success -> {
                    Log.e("VIEW MODEL RESULT ", response.data.toString())
                    _conversionMovie.value = MovieEvent.Success(response.data!!.movies)
                }
            }
        }
    }

    private val _conversionPopularMovie = MutableStateFlow<PopularMovieEvent>(PopularMovieEvent.Empty)
    val conversionPopularMovie : StateFlow<PopularMovieEvent> = _conversionPopularMovie

    fun getMovie(page: Int){
        viewModelScope.launch(Dispatchers.IO){
            _conversionPopularMovie.value = PopularMovieEvent.Loading
            when(val response = genreRepository.getMovie(page)){
                is Resource.Error -> _conversionPopularMovie.value = PopularMovieEvent.Error(response.message!!)
                is Resource.Success -> {
                    Log.e("VIEW MODEL RESULT ", response.data.toString())
                    _conversionPopularMovie.value = PopularMovieEvent.Success(response.data!!.movies)
                }
            }
        }
    }

    private val _conversionHighestRateMovie = MutableStateFlow<HighestRateMovieEvent>(HighestRateMovieEvent.Empty)
    val conversionHighestMovie : StateFlow<HighestRateMovieEvent> = _conversionHighestRateMovie

    fun getHighestRateMovie(){
        viewModelScope.launch(Dispatchers.IO){
            _conversionHighestRateMovie.value = HighestRateMovieEvent.Loading
            when(val response = genreRepository.getHighestRateMovie()){
                is Resource.Error -> _conversionHighestRateMovie.value = HighestRateMovieEvent.Error(response.message!!)
                is Resource.Success -> {
                    Log.e("VIEW MODEL RESULT ", response.data.toString())
                    _conversionHighestRateMovie.value = HighestRateMovieEvent.Success(response.data!!.movies)
                }
            }
        }
    }

}