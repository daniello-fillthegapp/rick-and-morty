package com.apiumhub.presentation.viewmodel

import com.apiumhub.domain.model.PaginatedCharacterListModel
import com.apiumhub.domain.usecase.GetCharactersPageUseCase
import com.apiumhub.domain.usecase.ObserveNetworkAvailabilityUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getCharactersPageUseCase = mock<GetCharactersPageUseCase>()
    private val observeNetworkAvailabilityUseCase = mock<ObserveNetworkAvailabilityUseCase>()

    private lateinit var viewModel: CharactersViewModel

    private val dummyPage = PaginatedCharacterListModel(
        items = listOf(/* your CharacterModel here */),
        nextPageIndex = 1,
        hasMoreItems = true
    )

    @Test
    fun loads_first_page_successfuly() = runBlocking {
        whenever(getCharactersPageUseCase.execute(0)).thenReturn(Result.success(dummyPage))

        viewModel = CharactersViewModel(getCharactersPageUseCase, observeNetworkAvailabilityUseCase)

        val state = viewModel.screenState.value
        assertTrue(state is CharactersScreenState.Loaded)
    }

    @Test
    fun shows_error_when_loading_first_page_fails() = runBlocking {
        whenever(getCharactersPageUseCase.execute(0)).thenReturn(Result.failure(Exception("Oops")))

        viewModel = CharactersViewModel(getCharactersPageUseCase, observeNetworkAvailabilityUseCase)

        val state = viewModel.screenState.value
        assertTrue(state is CharactersScreenState.Error)
    }
}