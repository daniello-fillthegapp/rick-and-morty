package com.fillthegapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun RickAndMortyNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AppRoute.Characters.route
    ) {
        addCharacterListRoute(onNavigateToDetails = { id -> navController.navigateToDetails(id) })
        addCharacterDetailsRoute(onNavigateBack = { navController.navigateUp() })
    }
}