package test.linkaja.testapp.homescreen.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import test.linkaja.testapp.homescreen.model.genres.Genre
import test.linkaja.testapp.homescreen.model.movie.Movie
import test.linkaja.testapp.homescreen.repository.MovieRepository
import test.linkaja.testapp.model.Resource
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository):ViewModel() {
    sealed class GenreEvent{
        class Success(val genresResponse: List<Genre>): GenreEvent()
        class Error(val errorMessage: String): GenreEvent()
        object Loading: GenreEvent()
        object Empty: GenreEvent()
    }

    sealed class MovieEvent{
        class Success(val movieItem: MutableList<Movie>): MovieEvent()
        class Error(val errorMessage: String): MovieEvent()
        object Loading: MovieEvent()
        object Empty: MovieEvent()
    }

    sealed class PopularMovieEvent{
        class Success(val movieItem: MutableList<Movie>): PopularMovieEvent()
        class Error(val errorMessage: String): PopularMovieEvent()
        object Loading: PopularMovieEvent()
        object Empty: PopularMovieEvent()
    }

    sealed class HighestRateMovieEvent{
        class Success(val movieItem: MutableList<Movie>): HighestRateMovieEvent()
        class Error(val errorMessage: String): HighestRateMovieEvent()
        object Loading: HighestRateMovieEvent()
        object Empty: HighestRateMovieEvent()
    }

    sealed class SearchEvent{
        class Success(val movieItem: MutableList<Movie>): SearchEvent()
        class Error(val errorMessage: String): SearchEvent()
        object Loading: SearchEvent()
        object Empty: SearchEvent()
    }

    private val _conversion = MutableStateFlow<GenreEvent>(GenreEvent.Empty)
    val conversion : StateFlow<GenreEvent> = _conversion

    fun getGenres(){
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = GenreEvent.Loading
            when(val response = movieRepository.getGenres()){
                is Resource.Error -> _conversion.value = GenreEvent.Error(response.message!!)
                is Resource.Success -> {
                    _conversion.value = GenreEvent.Success(response.data!!.genres)
                }
            }
        }
    }

    fun getLocalGenre(){
        viewModelScope.launch(Dispatchers.IO){
            _conversion.value = GenreEvent.Loading
            _conversion.value = GenreEvent.Success(movieRepository.getAllGenres())
        }
    }

    private val _conversionMovie = MutableStateFlow<MovieEvent>(MovieEvent.Empty)
    val conversionMovie : StateFlow<MovieEvent> = _conversionMovie

    fun getMovieByGenre(id: String, page: Int){
        viewModelScope.launch(Dispatchers.IO){
            _conversionMovie.value = MovieEvent.Loading
            when(val response = movieRepository.getMovieByGenre(id, page)){
                is Resource.Error -> _conversionMovie.value = MovieEvent.Error(response.message!!)
                is Resource.Success -> {
                    Log.e("BY GENRE RESULT ", response.data.toString())
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
            when(val response = movieRepository.getMovie(page)){
                is Resource.Error -> _conversionPopularMovie.value = PopularMovieEvent.Error(response.message!!)
                is Resource.Success -> {
                    Log.e("POPULAR RESULT ", response.data.toString())
                    _conversionPopularMovie.value = PopularMovieEvent.Success(response.data!!.movies)
                }
            }
        }
    }

    fun getLocalMovie(){
        viewModelScope.launch(Dispatchers.IO){
            _conversionPopularMovie.value = PopularMovieEvent.Loading
            _conversionPopularMovie.value = PopularMovieEvent.Success(movieRepository.getAllMovie())
        }
    }

    private val _conversionHighestRateMovie = MutableStateFlow<HighestRateMovieEvent>(HighestRateMovieEvent.Empty)
    val conversionHighestMovie : StateFlow<HighestRateMovieEvent> = _conversionHighestRateMovie

    fun getHighestRateMovie(){
        viewModelScope.launch(Dispatchers.IO){
            _conversionHighestRateMovie.value = HighestRateMovieEvent.Loading
            when(val response = movieRepository.getHighestRateMovie()){
                is Resource.Error -> _conversionHighestRateMovie.value = HighestRateMovieEvent.Error(response.message!!)
                is Resource.Success -> {
                    Log.e("HIGHEST RESULT ", response.data.toString())
                    _conversionHighestRateMovie.value = HighestRateMovieEvent.Success(response.data!!.movies)
                }
            }
        }
    }

    private val _conversionSearch = MutableStateFlow<SearchEvent>(SearchEvent.Empty)
    val conversionSearch : StateFlow<SearchEvent> = _conversionSearch

    fun searchMovie(query: String, page: Int){
        viewModelScope.launch(Dispatchers.IO){
            _conversionSearch.value = SearchEvent.Loading
            when(val response = movieRepository.searchMovie(query, page)){
                is Resource.Error -> _conversionSearch.value = SearchEvent.Error(response.message!!)
                is Resource.Success -> {
                    Log.e("SEARCH RESULT ", response.data.toString())
                    _conversionSearch.value = SearchEvent.Success(response.data!!.movies)
                }
            }
        }
    }

}