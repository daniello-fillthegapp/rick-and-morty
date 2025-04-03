package com.apiumhub.presentation.navigation

sealed class AppRoute(val route: String) {
    data object Characters : AppRoute(route = "characters")
    data object CharacterDetail : AppRoute(route = "character_detail/{id}")
}