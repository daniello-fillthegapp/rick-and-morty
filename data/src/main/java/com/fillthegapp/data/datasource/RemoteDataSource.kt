package com.fillthegapp.data.datasource

import androidx.annotation.IntRange
import com.fillthegapp.data.model.response.CharacterResponse
import com.fillthegapp.data.model.response.CharactersPageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

internal interface RemoteDataSource {

    @GET("character")
    suspend fun getCharacters(
        @Query("page") @IntRange(from = 1) page: Int
    ): CharactersPageResponse

    @GET("character/{character_id}")
    suspend fun getCharacter(
        @Path("character_id") id: String,
    ): CharacterResponse
}