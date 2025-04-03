package com.apiumhub.data.repository

import com.apiumhub.data.datasource.LocalDataSource
import com.apiumhub.data.datasource.RemoteDataSource
import com.apiumhub.data.model.entity.CharacterEntity
import com.apiumhub.data.model.entity.mapper.toDomain
import com.apiumhub.data.model.response.CharacterPageInfoResponse
import com.apiumhub.data.model.response.CharacterResponse
import com.apiumhub.data.model.response.CharactersPageResponse
import com.apiumhub.data.model.response.LocationResponse
import com.apiumhub.domain.model.CharacterModel
import com.apiumhub.domain.model.LocationModel
import com.apiumhub.domain.model.PaginatedCharacterListModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoInteractions
import org.mockito.kotlin.whenever
import retrofit2.Response
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterRepositoryImplTest {
    private lateinit var repository: CharacterRepositoryImpl
    private val remoteDataSource: RemoteDataSource = mock()
    private val localDataSource: LocalDataSource = mock()

    @Before
    fun setUp() {
        repository = CharacterRepositoryImpl(remoteDataSource, localDataSource)
    }

    @Test
    fun `getCharactersPage returns local data when available`() = runTest {
        // Given: Local data exists
        val index = 1
        val localCharacters = listOf(
            CharacterEntity(
                id = 1,
                name = "Rick",
                image = "image",
                status = "alive",
                species = "human",
                type = "",
                gender = "male",
                url = "url",
                creationDate = "",
                originName = "Earth",
                originUrl = "originUrl",
                locationName = "locationName",
                locationUrl = "locationUrl",
                episodeList = listOf("1", "2")
            )
        )
        val expectedModel = PaginatedCharacterListModel(
            hasMoreItems = true,
            nextPageIndex = 2,
            items = listOf(
                CharacterModel(
                    id = 1,
                    name = "Rick",
                    image = "image",
                    status = "alive",
                    species = "human",
                    type = "",
                    gender = "male",
                    url = "url",
                    creationDate = "",
                    origin = LocationModel(
                        name = "Earth",
                        url = "originUrl"
                    ),
                    location = LocationModel(
                        name = "locationName",
                        url = "locationUrl"
                    ),
                    episodeList = listOf("1", "2")
                )
            )
        )
        whenever(localDataSource.getCharactersPage(index)).thenReturn(localCharacters)

        // When: Calling getCharactersPage
        val result = repository.getCharactersPage(index)

        // Then: Should return local data mapped to domain and data is correctly mapped
        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().items.size)
        assertEquals(result, Result.success(expectedModel))
        verify(localDataSource).getCharactersPage(index)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun `getCharactersPage fetches from remote when local data is empty`() = runTest {
        // Given: Local data is empty
        val index = 1
        whenever(localDataSource.getCharactersPage(index)).thenReturn(emptyList())

        // And: Remote returns a valid response
        val remoteResponse = CharactersPageResponse(
            pageInfoResponse = CharacterPageInfoResponse(
                count = 1,
                pagesAmount = 2,
                previousPageUrl = "",
                nextPageUrl = "nextPageurl"
            ),
            characterResponseList = listOf(
                CharacterResponse(
                    id = 1,
                    name = "Rick",
                    image = "image",
                    status = "alive",
                    species = "human",
                    type = "",
                    gender = "male",
                    url = "url",
                    creationDate = "",
                    origin = LocationResponse(
                        name = "Earth",
                        url = "originUrl"
                    ),
                    location = LocationResponse(
                        name = "locationName",
                        url = "locationUrl"
                    ),
                    episodeList = listOf("1", "2")
                )
            )
        )

        whenever(remoteDataSource.getCharactersPage(index)).thenReturn(remoteResponse)

        val expectedModel = PaginatedCharacterListModel(
            hasMoreItems = true,
            nextPageIndex = 2,
            items = listOf(
                CharacterModel(
                    id = 1,
                    name = "Rick",
                    image = "image",
                    status = "alive",
                    species = "human",
                    type = "",
                    gender = "male",
                    url = "url",
                    creationDate = "",
                    origin = LocationModel(
                        name = "Earth",
                        url = "originUrl"
                    ),
                    location = LocationModel(
                        name = "locationName",
                        url = "locationUrl"
                    ),
                    episodeList = listOf("1", "2")
                )
            )
        )

        whenever(remoteDataSource.getCharactersPage(index)).thenReturn(remoteResponse)
        val mockResponseBody = ResponseBody.create(null, "mocked_image_data")
        val mockedResponse = Response.success(mockResponseBody)
        whenever(remoteDataSource.downloadMedia(any())).thenReturn(mockedResponse)

        // When: Calling getCharactersPage
        val result = repository.getCharactersPage(index)

        // Then: Data should be fetched from remote, stored locally and data is correctly mapped
        assertTrue(result.isSuccess)
        assertEquals(result, Result.success(expectedModel))
        assertEquals(1, result.getOrThrow().items.size)
        verify(remoteDataSource).getCharactersPage(index)
        verify(localDataSource).insertCharacters(any())
    }

    @Test
    fun `getCharacterDetails returns from local when available`() = runTest {
        // Given: Local data exists
        val id = 1
        val characterEntity = CharacterEntity(
            id = 1,
            name = "Rick",
            image = "image",
            status = "alive",
            species = "human",
            type = "",
            gender = "male",
            url = "url",
            creationDate = "",
            originName = "Earth",
            originUrl = "originUrl",
            locationName = "locationName",
            locationUrl = "locationUrl",
            episodeList = listOf("1", "2")
        )
        whenever(localDataSource.getCharacter(id)).thenReturn(characterEntity)

        val expectedModel = CharacterModel(
            id = 1,
            name = "Rick",
            image = "image",
            status = "alive",
            species = "human",
            type = "",
            gender = "male",
            url = "url",
            creationDate = "",
            origin = LocationModel(
                name = "Earth",
                url = "originUrl"
            ),
            location = LocationModel(
                name = "locationName",
                url = "locationUrl"
            ),
            episodeList = listOf("1", "2")
        )

        // When: Calling getCharacterDetails
        val result = repository.getCharacterDetails(id)

        // Then: Should return local data mapped to domain and data is correctly mapped
        assertTrue(result.isSuccess)
        assertEquals(result, Result.success(expectedModel))
        assertEquals(characterEntity.toDomain(), result.getOrThrow())
        verify(localDataSource).getCharacter(id)
        verifyNoInteractions(remoteDataSource)
    }

    @Test
    fun `getCharacterDetails fetches from remote when local data is missing`() = runTest {
        // Given: Local data does not exist
        val id = 1
        whenever(localDataSource.getCharacter(id)).thenReturn(null)

        // And: Remote returns a valid response
        val remoteCharacter = CharacterResponse(
            id = 1,
            name = "Rick",
            image = "image",
            status = "alive",
            species = "human",
            type = "",
            gender = "male",
            url = "url",
            creationDate = "",
            origin = LocationResponse(
                name = "Earth",
                url = "originUrl"
            ),
            location = LocationResponse(
                name = "locationName",
                url = "locationUrl"
            ),
            episodeList = listOf("1", "2")
        )
        whenever(remoteDataSource.getCharacterDetails(id)).thenReturn(remoteCharacter)

        val expectedModel = CharacterModel(
            id = 1,
            name = "Rick",
            image = "image",
            status = "alive",
            species = "human",
            type = "",
            gender = "male",
            url = "url",
            creationDate = "",
            origin = LocationModel(
                name = "Earth",
                url = "originUrl"
            ),
            location = LocationModel(
                name = "locationName",
                url = "locationUrl"
            ),
            episodeList = listOf("1", "2")
        )

        // When: Calling getCharacterDetails
        val result = repository.getCharacterDetails(id)

        // Then: Should fetch from remote and data is correctly mapped
        assertTrue(result.isSuccess)
        assertEquals(result, Result.success(expectedModel))
        assertEquals(remoteCharacter.toDomain(), result.getOrThrow())
        verify(remoteDataSource).getCharacterDetails(id)
    }

    @Test
    fun `getCharacterDetails fails when remote data also fails`() = runTest {
        // Given: Local data does not exist
        val id = 1
        whenever(localDataSource.getCharacter(id)).thenReturn(null)

        // And: Remote throws an error
        whenever(remoteDataSource.getCharacterDetails(id)).thenThrow(RuntimeException("Network error"))

        // When: Calling getCharacterDetails
        val result = repository.getCharacterDetails(id)

        // Then: Should return failure
        assertTrue(result.isFailure)
        verify(remoteDataSource).getCharacterDetails(id)
    }

    @Test
    fun `getCharactersPage fails when remote data also fails`() = runTest {
        // Given: Local data does not exist
        val index = 1
        whenever(localDataSource.getCharactersPage(index)).thenReturn(emptyList())

        // And: Remote throws an error
        whenever(remoteDataSource.getCharactersPage(index)).thenThrow(RuntimeException("Network error"))

        // When: Calling getCharactersPage
        val result = repository.getCharactersPage(index)

        // Then: Should return failure
        assertTrue(result.isFailure)
        verify(remoteDataSource).getCharactersPage(index)
    }
}