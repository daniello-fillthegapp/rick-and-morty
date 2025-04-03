package com.fillthegapp.presentation.model

import com.fillthegapp.domain.model.CharacterModel

data class CharacterViewData(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val creationDate: String,
    val originName: String,
    val locationName: String,
    val episodes: String
) {
    companion object {
        fun from(model: CharacterModel): CharacterViewData {
            return CharacterViewData(
                id = model.id,
                name = model.name,
                status = model.status,
                species = model.species,
                type = model.type,
                gender = model.gender,
                image = model.image,
                creationDate = model.creationDate,
                originName = model.origin.name,
                locationName = model.location.name,
                episodes = getChaptersViewData(model.episodeList)
            )
        }

        private fun getChaptersViewData(strings: List<String>): String {
            return strings.joinToString(", ") { it.substringAfterLast("/") }
        }
    }
}
