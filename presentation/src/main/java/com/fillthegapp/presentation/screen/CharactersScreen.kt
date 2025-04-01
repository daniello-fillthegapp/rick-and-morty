package com.fillthegapp.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CharactersScreen(
    onNavigateToDetails: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier
            .fillMaxSize()
            .clickable {
                onNavigateToDetails("id1")
            }, contentAlignment = Alignment.Center
    ) {
        Text("Character List Screen")
    }
}