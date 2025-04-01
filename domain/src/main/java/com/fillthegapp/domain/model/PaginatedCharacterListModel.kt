package com.fillthegapp.domain.model

data class PaginatedCharacterListModel(
    val items: List<CharacterModel>,
    val hasMoreItems: Boolean,
    val nextPageIndex: Int
)