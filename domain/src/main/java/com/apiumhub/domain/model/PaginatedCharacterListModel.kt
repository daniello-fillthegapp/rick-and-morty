package com.apiumhub.domain.model

data class PaginatedCharacterListModel(
    val hasMoreItems: Boolean,
    val nextPageIndex: Int,
    val items: List<CharacterModel>,
)