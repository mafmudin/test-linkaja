package test.linkaja.testapp.detailmovie.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import test.linkaja.testapp.detailmovie.model.MovieDetailResponse
import test.linkaja.testapp.detailmovie.repository.DetailMovieRepository
import test.linkaja.testapp.model.Resource
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val movieRepository: DetailMovieRepository
):ViewModel() {
    sealed class DetailMoveEvent{
        class Success(val movieDetail: MovieDetailResponse): DetailMoveEvent()
        class Error(val errorMessage: String): DetailMoveEvent()
        object Loading: DetailMoveEvent()
        object Empty: DetailMoveEvent()
    }

    private val _conversion = MutableStateFlow<DetailMoveEvent>(DetailMoveEvent.Empty)
    val conversion : StateFlow<DetailMoveEvent> = _conversion

    fun getDetailMovie(id: String){
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = DetailMoveEvent.Loading
            when(val response = movieRepository.getMovieDetail(id)){
                is Resource.Error -> _conversion.value = DetailMoveEvent.Error(response.message!!)
                is Resource.Success -> {
                    Log.e("DETAILVIEW MODEL ", response.data.toString())
                    _conversion.value = DetailMoveEvent.Success(response.data!!)
                }
            }
        }
    }
}