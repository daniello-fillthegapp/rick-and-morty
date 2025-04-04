package com.apiumhub.domain.usecase

import com.apiumhub.domain.model.PaginatedCharacterListModel
import com.apiumhub.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharactersPageUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend fun execute(index: Int): Result<PaginatedCharacterListModel> {
        return characterRepository.getCharactersPage(index)
    }
}