package com.fillthegapp.data.repository

import com.fillthegapp.data.datasource.RemoteDataSource
import com.fillthegapp.domain.model.CharacterModel
import com.fillthegapp.domain.model.PaginatedCharacterListModel
import com.fillthegapp.domain.repository.CharacterRepository

class CharacterRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : CharacterRepository {
    override suspend fun getCharactersPage(index: Int): Result<PaginatedCharacterListModel> {
        return runCatching {
            val charactersPageResponse = this.remoteDataSource.getCharactersPage(index)

            PaginatedCharacterListModel(
                items = charactersPageResponse.characterResponseList.map { it.toDomain() },
                hasMoreItems = charactersPageResponse.pageInfoResponse.nextPageUrl.isNotEmpty(),
                nextPageIndex = index.inc()
            )
        }
    }

    override suspend fun getCharacterDetails(id: String): Result<CharacterModel> {
        return runCatching {
            this.remoteDataSource.getCharacterDetails(id).toDomain()
        }
    }
}