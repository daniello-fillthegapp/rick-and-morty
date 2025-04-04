package com.apiumhub.data.model.entity.mapper

import com.apiumhub.data.model.entity.CharacterEntity
import com.apiumhub.data.model.response.CharacterResponse
import com.apiumhub.domain.model.CharacterModel
import com.apiumhub.domain.model.LocationModel

fun CharacterEntity.toDomain() = CharacterModel(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    image = image,
    url = url,
    creationDate = creationDate,
    origin = LocationModel(
        name = originName,
        url = originUrl
    ),
    location = LocationModel(
        name = locationName,
        url = locationUrl
    ),
    episodeList = episodeList,
)

fun CharacterResponse.toEntity() = CharacterEntity(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    image = image,
    url = url,
    creationDate = creationDate,
    originName = origin.name,
    originUrl = origin.url,
    locationName = location.name,
    locationUrl = location.url,
    episodeList = episodeList,
)