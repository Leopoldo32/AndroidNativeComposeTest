package com.example.testandroidcompose.Interface

import com.example.testandroidcompose.Model.CharacterResponse
import com.example.testandroidcompose.Model.CharacterDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    @GET("character")
    suspend fun getAllCharacters(): CharacterResponse

    @GET("character/{id}")
    suspend fun getCharacterById(@Path("id") id: Int): CharacterDetail
}