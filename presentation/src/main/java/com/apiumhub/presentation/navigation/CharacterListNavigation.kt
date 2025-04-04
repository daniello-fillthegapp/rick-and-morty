package com.apiumhub.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.apiumhub.presentation.screen.CharactersScreen

fun NavGraphBuilder.addCharacterListRoute(
    onNavigateToDetails: (Int) -> Unit,
) {
    composable(route = AppRoute.Characters.route) {
        CharactersScreen(
            onNavigateToDetails = onNavigateToDetails
        )
    }
}