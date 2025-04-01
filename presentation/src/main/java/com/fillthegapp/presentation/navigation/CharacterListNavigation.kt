package com.fillthegapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.fillthegapp.presentation.screen.CharactersScreen

fun NavGraphBuilder.addCharacterListRoute(
    onNavigateToDetails: (String) -> Unit,
) {
    composable(route = AppRoute.Characters.route) {
        CharactersScreen(
            onNavigateToDetails = onNavigateToDetails
        )
    }
}