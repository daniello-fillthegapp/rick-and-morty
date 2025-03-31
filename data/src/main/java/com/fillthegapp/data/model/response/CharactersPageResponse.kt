package com.fillthegapp.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CharactersPageResponse(
    @SerialName("info") val pageInfoResponse: CharacterPageInfoResponse,
    @SerialName("results") val characterResponseList: List<CharacterResponse>,
)

@Serializable
internal data class CharacterPageInfoResponse(
    @SerialName("count") val count: Int,
    @SerialName("pages") val pagesAmount: Int,
    @SerialName("next") val nextPageUrl: String,
    @SerialName("prev") val previousPageUrl: String,
)