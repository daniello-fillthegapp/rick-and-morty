package com.fillthegapp.data.datasource

import androidx.annotation.IntRange
import com.fillthegapp.data.model.response.CharacterResponse
import com.fillthegapp.data.model.response.CharactersPageResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RemoteDataSource {

    @GET("character")
    suspend fun getCharactersPage(
        @Query("page") @IntRange(from = 1) page: Int
    ): CharactersPageResponse

    @GET("character/{character_id}")
    suspend fun getCharacterDetails(
        @Path("character_id") characterId: String,
    ): CharacterResponse
}