package com.fillthegapp.domain.usecase

import com.fillthegapp.domain.model.CharacterModel
import com.fillthegapp.domain.repository.CharacterRepository
import javax.inject.Inject

class GetCharacterDetailsUseCase @Inject constructor(
    private val characterRepository: CharacterRepository
) {
    suspend fun execute(id: String): Result<CharacterModel> {
        return characterRepository.getCharacterDetails(id)
    }
}