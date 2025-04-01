package com.fillthegapp.domain.usecase

import com.fillthegapp.domain.model.PaginatedCharacterListModel
import com.fillthegapp.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersPageUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend fun execute(index: Int): Result<PaginatedCharacterListModel> {
        return characterRepository.getCharactersPage(index)
    }
}