package com.apiumhub.domain.usecase

import com.apiumhub.domain.model.CharacterModel
import com.apiumhub.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend fun execute(id: Int): Result<CharacterModel> {
        return characterRepository.getCharacterDetails(id)
    }
}