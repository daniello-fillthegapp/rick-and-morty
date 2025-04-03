package com.fillthegapp.data.repository

import com.fillthegapp.data.datasource.LocalDataSource
import com.fillthegapp.data.datasource.RemoteDataSource
import com.fillthegapp.data.model.entity.CharacterEntity
import com.fillthegapp.data.model.entity.mapper.toDomain
import com.fillthegapp.data.model.entity.mapper.toEntity
import com.fillthegapp.domain.model.CharacterModel
import com.fillthegapp.domain.model.PaginatedCharacterListModel
import com.fillthegapp.domain.repository.CharacterRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val LAST_PAGE_INDEX = 42

class CharacterRepositoryImpl(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
) : CharacterRepository {
    override suspend fun getCharactersPage(index: Int): Result<PaginatedCharacterListModel> {
        return withContext(Dispatchers.IO) {
            runCatching {
                val localCharacters = localDataSource.getCharactersPage(index)

                if (localCharacters.isNotEmpty()) {
                    return@runCatching PaginatedCharacterListModel(
                        items = localCharacters.map { it.toDomain() },
                        hasMoreItems = index < LAST_PAGE_INDEX,
                        nextPageIndex = index.inc()
                    )
                }

                val charactersPageResponse = remoteDataSource.getCharactersPage(index)
                val characterEntities = charactersPageResponse.characterResponseList.map { it.toEntity() }
                val localMediaCharacterEntities = downloadCharacterEntitiesMedia(characterEntities)

                localDataSource.insertCharacters(localMediaCharacterEntities)

                PaginatedCharacterListModel(
                    items = localMediaCharacterEntities.map { it.toDomain() },
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
                localDataSource.getCharacter(id)?.toDomain()
                    ?: remoteDataSource.getCharacterDetails(id).toDomain()
            }
        }
    }

    private suspend fun downloadCharacterEntitiesMedia(entities: List<CharacterEntity>): List<CharacterEntity> {
        return entities.map { character ->
            character.copy(image = downloadImage(character.image))
        }
    }

    private suspend fun downloadImage(url: String): String {
        val remoteData = remoteDataSource.downloadMedia(url)
        val body = remoteData.body()?.bytes()
        if (remoteData.isSuccessful && body != null) {
            val localStoredFilePath = localDataSource.storeMediaImage(
                imageUrl = url,
                imageData = body
            )
            if (localStoredFilePath != null) {
                return localStoredFilePath
            }
        }

        return url
    }
}