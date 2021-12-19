package test.linkaja.testapp.homescreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import test.linkaja.testapp.homescreen.model.genres.Genre
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
}