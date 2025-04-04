package com.apiumhub.presentation.model

import com.apiumhub.domain.model.CharacterModel

data class CharacterViewData(
    val id: Int,
    val name: String,
    val species: String,
    val type: String,
    val gender: String,
    val image: String,
    val creationDate: String,
    val originName: String,
    val locationName: String,
    val episodesLabel: String,
    val episodesAmount: Int,
    val status: CharacterStatus,
) {
    val shouldShowEpisodeTab = episodesAmount >= 10

    companion object {
        fun from(model: CharacterModel): CharacterViewData {
            return CharacterViewData(
                id = model.id,
                name = model.name,
                status = CharacterStatus.from(model.status),
                species = model.species,
                type = model.type,
                gender = model.gender,
                image = model.image,
                creationDate = model.creationDate,
                originName = model.origin.name,
                locationName = model.location.name,
                episodesLabel = getChaptersViewData(model.episodeList),
                episodesAmount = model.episodeList.size
            )
        }

        private fun getChaptersViewData(strings: List<String>): String {
            return strings.joinToString(", ") { it.substringAfterLast("/") }
        }
    }
}

enum class CharacterStatus(val status: String) {
    ALIVE("Alive"), DEAD("Dead"), UNKNOWN("Unknown");

    companion object {
        fun from(status: String): CharacterStatus {
            return CharacterStatus.entries.firstOrNull { it.status == status } ?: UNKNOWN
        }
    }
}
