package com.apiumhub.data.model.response

import com.apiumhub.domain.model.CharacterModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
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
) {
    fun toDomain(): CharacterModel {
        return CharacterModel(
            id = this.id,
            name = this.name,
            status = this.status,
            species = this.species,
            type = this.type,
            gender = this.gender,
            image = this.image,
            url = this.url,
            creationDate = this.creationDate,
            origin = this.origin.toDomain(),
            location = this.location.toDomain(),
            episodeList = this.episodeList
        )
    }
}