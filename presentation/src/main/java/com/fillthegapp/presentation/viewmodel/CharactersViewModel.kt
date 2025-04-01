package com.fillthegapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fillthegapp.domain.model.PaginatedCharacterListModel
import com.fillthegapp.domain.usecase.GetCharactersPageUseCase
import com.fillthegapp.presentation.model.CharacterViewData
import com.fillthegapp.presentation.model.PaginatedCharacterListViewData
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
    private val getCharactersPageUseCase: GetCharactersPageUseCase
) : ViewModel() {

    companion object {
        private const val FIRST_PAGE_INDEX = 1
    }

    private val _screenState =
        MutableStateFlow<CharactersScreenState>(CharactersScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<CharacterListScreenAction>()
    val screenAction = _screenAction.asSharedFlow()

    private var nextPageIndex: Int = FIRST_PAGE_INDEX

    init {
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

    private fun loadNextPage() {
        if (!_screenState.value.canLoad && nextPageIndex != FIRST_PAGE_INDEX) return

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
                        items = mergeItems(
                            oldItems = current.data.items,
                            newItems = newData.items.map { CharacterViewData.from(it) }
                        )
                    )
                )
            } else {
                CharactersScreenState.Loaded(
                    data = PaginatedCharacterListViewData.from(newData)
                )
            }
        }
    }

    private fun onPageLoadFailure(error: Throwable) {
        _screenState.update {
            Log.e(this.javaClass.name, error.message, error)
            CharactersScreenState.Error
        }
    }

    private fun mergeItems(
        oldItems: List<CharacterViewData>,
        newItems: List<CharacterViewData>,
    ): List<CharacterViewData> {
        if (oldItems.isEmpty()) return newItems
        if (newItems.isEmpty()) return oldItems

        val mergedItems = oldItems.toMutableList()
        val lastOldItem = oldItems.last()
        val firstNewItem = newItems.first()

        if (lastOldItem.id == firstNewItem.id) {
            mergedItems.addAll(newItems.drop(1))
        } else {
            mergedItems.addAll(newItems)
        }

        return mergedItems
    }

}

open class CharactersScreenState(
    val canLoad: Boolean
) {
    object Loading : CharactersScreenState(false)
    object Error : CharactersScreenState(true)
    data class Loaded(
        val data: PaginatedCharacterListViewData,
    ) : CharactersScreenState(data.hasMoreItems)
}

sealed class CharacterListScreenAction {
    data class NavigateToCharacterDetailAction(val characterId: Int) : CharacterListScreenAction()
}