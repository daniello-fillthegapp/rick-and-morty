package com.apiumhub.domain.repository

import com.apiumhub.domain.model.CharacterModel
import com.apiumhub.domain.model.PaginatedCharacterListModel

interface CharacterRepository {
    suspend fun getCharactersPage(index: Int): Result<PaginatedCharacterListModel>

    suspend fun getCharacterDetails(id: Int): Result<CharacterModel>
}