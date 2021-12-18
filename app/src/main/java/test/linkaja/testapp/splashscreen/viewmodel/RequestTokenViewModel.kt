package test.linkaja.testapp.splashscreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import test.linkaja.testapp.model.Resource
import test.linkaja.testapp.splashscreen.model.RequestTokenResponse
import test.linkaja.testapp.splashscreen.repository.RequestTokenRepository
import javax.inject.Inject

@HiltViewModel
class RequestTokenViewModel @Inject constructor(private val repository: RequestTokenRepository): ViewModel(){
    sealed class RequestTokenEvent{
        class Success(val requestTokenResponse:RequestTokenResponse):RequestTokenEvent()
        class Failure(val errorText:String):RequestTokenEvent()
        object Loading:RequestTokenEvent()
        object Empty:RequestTokenEvent()
    }

    private val _conversion = MutableStateFlow<RequestTokenEvent>(RequestTokenEvent.Empty)
    val conversion: StateFlow<RequestTokenEvent> = _conversion

    fun requestToken(){
        viewModelScope.launch(Dispatchers.IO){
            _conversion.value = RequestTokenEvent.Loading
            when(val response = repository.requestToken()){
                is Resource.Error -> _conversion.value = RequestTokenEvent.Failure(response.message!!)
                is Resource.Success -> {
                    val success = response.data!!.success
                    if (success){
                        _conversion.value = RequestTokenEvent.Success(response.data!!)
                    }else{
                        _conversion.value = RequestTokenEvent.Failure("Unexpected Error")
                    }
                }
            }
        }
    }
}