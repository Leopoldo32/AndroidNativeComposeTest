package com.example.testandroidcompose

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.testandroidcompose.Model.CharacterDetail
import kotlinx.coroutines.launch


class CharactherListViewModel: ViewModel() {
    private val repository = CharacterRepository()

    private val _characters = MutableLiveData<List<CharacterDetail>>()
    val characters: MutableLiveData<List<CharacterDetail>>
        get() = _characters

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _error = MutableLiveData<Exception?>()
    val error: LiveData<Exception?>
        get() = _error

    private val _searchTerm = MutableLiveData<String>("")
    val searchTerm: LiveData<String>
        get() = _searchTerm

    private val _filteredCharacters = MutableLiveData<List<CharacterDetail>>(emptyList())
    val filteredCharacters: LiveData<List<CharacterDetail>>
        get() = _filteredCharacters

    init{
        loadCharacters()
    }

    fun loadCharacters(){
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try{
                val response = repository.getAllCharacters()
                _characters.value = response.results
                _filteredCharacters.value = response.results
            }catch (e: Exception){
                _error.value = e
            }finally {
                _isLoading.value = false
            }
        }
    }

}
