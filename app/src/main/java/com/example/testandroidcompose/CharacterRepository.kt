package com.example.testandroidcompose

import com.example.testandroidcompose.Model.CharacterResponse
import com.example.testandroidcompose.Model.CharacterDetail

class CharacterRepository {
    private val apiService = ApiService.create()

    suspend fun getAllCharacters(): CharacterResponse{
        return apiService.getAllCharacters()
    }

    suspend fun getAllCharacterById(id: Int): CharacterDetail{
        return apiService.getCharacterById(id)
    }
}