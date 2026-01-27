package com.apiumhub.presentation.widget

import com.apiumhub.domain.usecase.GetCharactersPageUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface WidgetEntryPoint {
    fun getCharactersPageUseCase(): GetCharactersPageUseCase
}