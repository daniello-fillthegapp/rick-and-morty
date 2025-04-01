package com.fillthegapp.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.fillthegapp.presentation.screen.CharacterDetailsScreen

fun NavController.navigateToDetails(id: String, options: NavOptions? = null) {
    navigate(route = AppRoute.CharacterDetail.route.replace("{id}", id), options)
}

fun NavGraphBuilder.addCharacterDetailsRoute(
    onNavigateBack: () -> Unit
) {
    composable(route = AppRoute.CharacterDetail.route, arguments = listOf(navArgument("id") {
        type = NavType.StringType
    })) {
        CharacterDetailsScreen(
            onNavigateBack = onNavigateBack
        )
    }
}