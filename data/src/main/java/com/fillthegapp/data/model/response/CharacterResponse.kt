package com.fillthegapp.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class CharacterResponse(
    @SerialName("id") val id: Int,
    @SerialName("name") val name: String,
    @SerialName("status") val status: String,
    @SerialName("species") val species: String,
    @SerialName("type") val type: String,
    @SerialName("gender") val gender: String,
    @SerialName("image") val image: String,
    @SerialName("url") val url: String,
    @SerialName("created") val creationDate: String,
    @SerialName("origin") val origin: LocationResponse,
    @SerialName("location") val location: LocationResponse,
    @SerialName("episode") val episodeList: List<String>,
)

@Serializable
internal data class LocationResponse(
    @SerialName("name") val name: String,
    @SerialName("url") val url: String,
)