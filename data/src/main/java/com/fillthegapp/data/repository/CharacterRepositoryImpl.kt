package com.fillthegapp.data.repository

import com.fillthegapp.data.datasource.RemoteDataSource
import com.fillthegapp.domain.model.CharacterModel
import com.fillthegapp.domain.model.PaginatedCharacterListModel
import com.fillthegapp.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharacterRepositoryImpl(
    private val remoteDataSource: RemoteDataSource
) : CharacterRepository {
    override suspend fun getCharactersPage(index: Int): Result<PaginatedCharacterListModel> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val charactersPageResponse = remoteDataSource.getCharactersPage(index)

                PaginatedCharacterListModel(
                    items = charactersPageResponse.characterResponseList.map { it.toDomain() },
                    hasMoreItems = charactersPageResponse.pageInfoResponse.nextPageUrl.orEmpty()
                        .isNotEmpty(),
                    nextPageIndex = index.inc()
                )
            }
        }
    }

    override suspend fun getCharacterDetails(id: Int): Result<CharacterModel> {
        return withContext(Dispatchers.IO) {
            runCatching {
                remoteDataSource.getCharacterDetails(id).toDomain()
            }
        }
    }
}