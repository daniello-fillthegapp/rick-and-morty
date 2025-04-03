package com.apiumhub.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apiumhub.domain.model.PaginatedCharacterListModel
import com.apiumhub.domain.usecase.GetCharactersPageUseCase
import com.apiumhub.domain.usecase.GetNetworkAvailabilityUseCase
import com.apiumhub.presentation.model.CharacterViewData
import com.apiumhub.presentation.model.PaginatedCharacterListViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val getCharactersPageUseCase: GetCharactersPageUseCase,
    private val getNetworkAvailabilityUseCase: GetNetworkAvailabilityUseCase
) : ViewModel() {

    companion object {
        private const val FIRST_PAGE_INDEX = 0
    }

    private val _networkState = MutableStateFlow(true)
    val networkState = _networkState.asStateFlow()

    private val _screenState =
        MutableStateFlow<CharactersScreenState>(CharactersScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<CharacterListScreenAction>()
    val screenAction = _screenAction.asSharedFlow()

    private var nextPageIndex: Int = FIRST_PAGE_INDEX

    init {
        getConnectivity()
        loadNextPage()
    }

    fun onMoreItemsRequested() {
        loadNextPage()
    }

    fun onRetryClicked() {
        loadNextPage()
    }

    fun onCharacterClicked(characterId: Int) {
        viewModelScope.launch {
            _screenAction.emit(CharacterListScreenAction.NavigateToCharacterDetailAction(characterId))
        }
    }

    private fun getConnectivity() {
        viewModelScope.launch {
            getNetworkAvailabilityUseCase.execute().collect {
                if (!_networkState.value && it && _screenState.value.canLoad) {
                    loadNextPage()
                }
                _networkState.emit(it)
            }
        }
    }

    private fun loadNextPage() {
        if (!_screenState.value.canLoad && nextPageIndex != FIRST_PAGE_INDEX) return

        _screenState.update { current ->
            if (current is CharactersScreenState.Loaded) {
                current.copy(isLoadingMoreData = true)
            } else {
                CharactersScreenState.Loading
            }
        }

        viewModelScope.launch {
            getCharactersPageUseCase.execute(index = nextPageIndex)
                .fold(
                    onSuccess = ::onPageLoadSuccess,
                    onFailure = ::onPageLoadFailure,
                )
        }
    }

    private fun onPageLoadSuccess(newData: PaginatedCharacterListModel) {
        _screenState.update { current ->
            nextPageIndex = newData.nextPageIndex
            if (current is CharactersScreenState.Loaded) {
                current.copy(
                    data = current.data.copy(
                        hasMoreItems = newData.hasMoreItems,
                        nextPageIndex = newData.nextPageIndex,
                        items = current.data.items + newData.items.map { CharacterViewData.from(it) }
                    ),
                    isErrorLoadingMore = false,
                    isLoadingMoreData = false,
                )
            } else {
                CharactersScreenState.Loaded(
                    data = PaginatedCharacterListViewData.from(newData),
                    isErrorLoadingMore = false,
                    isLoadingMoreData = false,
                )
            }
        }
    }

    private fun onPageLoadFailure(error: Throwable) {
        Log.e(this.javaClass.name, error.message, error)
        val currentState = _screenState.value

        if (currentState is CharactersScreenState.Loading) {
            _screenState.update { CharactersScreenState.Error }
        } else if (currentState is CharactersScreenState.Loaded) {
            _screenState.update {
                currentState.copy(
                    isErrorLoadingMore = true
                )
            }
        }
    }
}

open class CharactersScreenState(
    val canLoad: Boolean
) {
    object Loading : CharactersScreenState(false)
    object Error : CharactersScreenState(true)
    data class Loaded(
        val isErrorLoadingMore: Boolean,
        val isLoadingMoreData: Boolean,
        val data: PaginatedCharacterListViewData,
    ) : CharactersScreenState(data.hasMoreItems && (!isLoadingMoreData || isErrorLoadingMore))
}

sealed class CharacterListScreenAction {
    data class NavigateToCharacterDetailAction(val characterId: Int) : CharacterListScreenAction()
}