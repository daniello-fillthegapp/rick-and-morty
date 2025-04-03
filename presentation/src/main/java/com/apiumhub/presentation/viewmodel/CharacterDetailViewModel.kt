package com.apiumhub.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apiumhub.domain.model.CharacterModel
import com.apiumhub.domain.usecase.GetCharacterDetailsUseCase
import com.apiumhub.presentation.model.CharacterViewData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharacterDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCharacterDetailsUseCase: GetCharacterDetailsUseCase,
) : ViewModel() {

    private val id: Int = savedStateHandle.get<Int>("id")
        ?: throw IllegalArgumentException("Required parameter id is not present")

    private val _screenState =
        MutableStateFlow<CharacterDetailScreenState>(CharacterDetailScreenState.Loading)
    val screenState = _screenState.asStateFlow()

    private val _screenAction = MutableSharedFlow<CharacterDetailScreenAction>()
    val screenAction = _screenAction.asSharedFlow()

    init {
        loadPage()
    }

    fun onRetryClicked() {
        loadPage()
    }

    fun onNavigateBackClicked() {
        viewModelScope.launch {
            _screenAction.emit(CharacterDetailScreenAction.NavigateBackAction)
        }
    }

    private fun loadPage() {
        viewModelScope.launch {
            getCharacterDetailsUseCase.execute(id).fold(
                onSuccess = ::onRequestDetailsSuccess,
                onFailure = ::onRequestDetailsFailure
            )
        }
    }

    private fun onRequestDetailsSuccess(data: CharacterModel) {
        _screenState.update {
            CharacterDetailScreenState.Loaded(CharacterViewData.from(data))
        }
    }

    private fun onRequestDetailsFailure(error: Throwable) {
        _screenState.update {
            Log.e(this.javaClass.name, error.message, error)
            CharacterDetailScreenState.Error
        }
    }
}

sealed class CharacterDetailScreenState {
    object Loading : CharacterDetailScreenState()
    object Error : CharacterDetailScreenState()
    data class Loaded(
        val data: CharacterViewData,
    ) : CharacterDetailScreenState()
}

sealed class CharacterDetailScreenAction {
    object NavigateBackAction : CharacterDetailScreenAction()
}