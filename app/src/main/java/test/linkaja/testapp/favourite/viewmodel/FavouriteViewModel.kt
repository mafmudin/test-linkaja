package test.linkaja.testapp.favourite.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import test.linkaja.testapp.favourite.repository.FavoriteRepository
import test.linkaja.testapp.homescreen.model.movie.Movie
import test.linkaja.testapp.model.Resource
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    private val favouriteRepository: FavoriteRepository
): ViewModel() {
    sealed class FavouriteEvent{
        class Success(val movies: MutableList<Movie>): FavouriteEvent()
        class Error(val errorMessage: String): FavouriteEvent()
        object Loading: FavouriteEvent()
        object Empty: FavouriteEvent()
    }

    private val _conversion = MutableStateFlow<FavouriteEvent>(
        FavouriteEvent.Empty)
    val conversion : StateFlow<FavouriteEvent> = _conversion

    fun getFavouriteMovie(){
        viewModelScope.launch(Dispatchers.IO) {
            _conversion.value = FavouriteEvent.Loading
            when(val response = favouriteRepository.getFavourite()){
                is Resource.Error -> _conversion.value = FavouriteEvent.Error(response.message!!)
                is Resource.Success -> {
                    Log.e("FAVOURITE MODEL ", response.data.toString())
                    _conversion.value = FavouriteEvent.Success(response.data!!.movies)
                }
            }
        }
    }
}