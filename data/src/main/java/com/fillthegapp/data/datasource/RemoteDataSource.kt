package com.fillthegapp.data.datasource

import androidx.annotation.IntRange
import com.fillthegapp.data.model.response.CharacterResponse
import com.fillthegapp.data.model.response.CharactersPageResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Streaming
import retrofit2.http.Url

interface RemoteDataSource {

    @GET("character")
    suspend fun getCharactersPage(
        @Query("page") @IntRange(from = 1) page: Int
    ): CharactersPageResponse

    @GET("character/{character_id}")
    suspend fun getCharacterDetails(
        @Path("character_id") characterId: Int,
    ): CharacterResponse

    @Streaming
    @GET
    suspend fun downloadMedia(@Url mediaUrl: String): Response<ResponseBody>
}