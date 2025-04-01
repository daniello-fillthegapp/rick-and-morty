package com.fillthegapp.domain.model

data class CharacterModel(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val url: String,
    val creationDate: String,
    val origin: LocationModel,
    val location: LocationModel,
    val episodeList: List<String>
)
