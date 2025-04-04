package com.apiumhub.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.apiumhub.domain.model.CharacterModel
import com.apiumhub.domain.model.LocationModel
import com.apiumhub.domain.usecase.GetCharacterDetailsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailViewModelTest {

    private val testDispatcher = Dispatchers.Unconfined

    private lateinit var viewModel: CharacterDetailViewModel

    private val getCharacterDetailsUseCase = mock<GetCharacterDetailsUseCase>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `should emit Loaded state when use case returns success`() = runBlocking {
        //Given
        val expectedResult = CharacterModel(
            id = 1,
            name = "name",
            image = "image",
            status = "status",
            species = "species",
            type = "type",
            gender = "gender",
            url = "url",
            creationDate = "creationDate",
            origin = LocationModel(
                name = "originName",
                url = "originUrl"
            ),
            location = LocationModel(
                name = "locationName",
                url = "locationUrl"
            ),
            episodeList = listOf("1", "2")
        )

        whenever(getCharacterDetailsUseCase.execute(1)).thenReturn(Result.success(expectedResult))

        viewModel = CharacterDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("id" to 1)),
            getCharacterDetailsUseCase = getCharacterDetailsUseCase
        )


        // Then
        val state = viewModel.screenState.value
        assertTrue(state is CharacterDetailScreenState.Loaded)
        assertEquals("name", (state as CharacterDetailScreenState.Loaded).data.name)
    }

    @Test
    fun `should emit Error state when use case returns failure`() = runBlocking {
        //Given
        whenever(getCharacterDetailsUseCase.execute(1)).thenReturn(Result.failure(Exception("Oops")))

        viewModel = CharacterDetailViewModel(
            savedStateHandle = SavedStateHandle(mapOf("id" to 1)),
            getCharacterDetailsUseCase = getCharacterDetailsUseCase
        )

        // Then
        val state = viewModel.screenState.value
        assertTrue(state is CharacterDetailScreenState.Error)
    }
}