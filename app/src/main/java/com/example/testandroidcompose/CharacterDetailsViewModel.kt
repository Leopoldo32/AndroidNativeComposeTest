package com.example.testandroidcompose

import androidx.compose.runtime.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.testandroidcompose.Interface.ApiInterface
import com.example.testandroidcompose.Model.CharacterDetail
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CharacterDetailsViewModel: ViewModel() {
    private val repository = CharacterRepository()

    private val _characterDetails = MutableLiveData<CharacterDetail>()
    val characterDetails: LiveData<CharacterDetail>
        get() = _characterDetails

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<Exception?>()
    val error: LiveData<Exception?>
        get() = _error

    suspend fun getCharacterDetailId(characterId: Int): CharacterDetail? {
        _isLoading.value = true
        _error.value = null
        try{
            val response = repository.getAllCharacterById(characterId)
            _characterDetails.value = response
        }catch (e: Exception){
            _error.value = e
        }finally {
            _isLoading.value = false
        }

        return _characterDetails.value
    }
}
