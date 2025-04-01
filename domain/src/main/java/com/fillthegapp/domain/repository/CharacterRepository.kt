package com.fillthegapp.domain.repository

import com.fillthegapp.domain.model.CharacterModel
import com.fillthegapp.domain.model.PaginatedCharacterListModel

interface CharacterRepository {
    suspend fun getCharactersPage(index: Int): Result<PaginatedCharacterListModel>

    suspend fun getCharacterDetails(id: Int): Result<CharacterModel>
}