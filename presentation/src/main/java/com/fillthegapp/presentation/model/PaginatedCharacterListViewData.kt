package com.fillthegapp.presentation.model

import com.fillthegapp.domain.model.PaginatedCharacterListModel

class PaginatedCharacterListViewData(
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