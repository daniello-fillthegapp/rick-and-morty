package com.apiumhub.presentation.model

import com.apiumhub.domain.model.PaginatedCharacterListModel

data class PaginatedCharacterListViewData(
    val hasMoreItems: Boolean,
    val nextPageIndex: Int,
    val items: List<CharacterViewData>,
) {
    companion object {
        fun from(model: PaginatedCharacterListModel): PaginatedCharacterListViewData {
            return PaginatedCharacterListViewData(
                hasMoreItems = model.hasMoreItems,
                nextPageIndex = model.nextPageIndex,
                items = model.items.map { CharacterViewData.from(it) }
            )
        }
    }
}