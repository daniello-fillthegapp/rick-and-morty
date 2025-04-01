package com.fillthegapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.fillthegapp.presentation.R
import com.fillthegapp.presentation.model.CharacterViewData
import com.fillthegapp.presentation.model.PaginatedCharacterListViewData
import com.fillthegapp.presentation.ui.component.ErrorView
import com.fillthegapp.presentation.ui.component.LoaderView
import com.fillthegapp.presentation.ui.theme.Spacing
import com.fillthegapp.presentation.viewmodel.CharactersScreenState
import com.fillthegapp.presentation.viewmodel.CharactersViewModel

@Composable
fun CharactersScreen(
    onNavigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharactersViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsState()

    CharactersScreenContent(
        modifier = modifier,
        state = screenState,
        onRetryClicked = viewModel::onRetryClicked,
        onMoreItemsRequested = viewModel::onMoreItemsRequested,
        onCharacterClicked = onNavigateToDetails
    )
}

@Composable
private fun CharactersScreenContent(
    onRetryClicked: () -> Unit,
    onMoreItemsRequested: () -> Unit,
    onCharacterClicked: (Int) -> Unit,
    modifier: Modifier,
    state: CharactersScreenState,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (val currentState = state) {
            is CharactersScreenState.Loading -> {
                LoaderView()
            }

            is CharactersScreenState.Error -> {
                ErrorView(onRetryClicked = onRetryClicked)
            }

            is CharactersScreenState.Loaded -> {
                CharacterListView(
                    data = currentState.data,
                    onMoreItemsRequested = onMoreItemsRequested,
                    onCharacterClicked = onCharacterClicked
                )
            }
        }
    }
}

@Composable
private fun CharacterListView(
    onMoreItemsRequested: () -> Unit,
    onCharacterClicked: (Int) -> Unit,
    modifier: Modifier = Modifier,
    data: PaginatedCharacterListViewData,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        data.items.forEachIndexed { index, item ->
            if (index > 0) {
                item { Spacer(modifier = Modifier.height(Spacing.large)) }
            }

            item {
                CharacterItemView(
                    modifier = Modifier.padding(
                        horizontal = Spacing.medium,
                        vertical = Spacing.small
                    ),
                    data = item,
                    onClick = onCharacterClicked
                )
            }

            // To do the infinity scroll effect, just 2 items before the end start loading the next page
            if (data.hasMoreItems && index < data.items.size - 2) {
                item {
                    LaunchedEffect(Unit) {
                        onMoreItemsRequested()
                    }
                }
            }
        }
    }
}

@Composable
fun CharacterItemView(
    onClick: (Int) -> Unit,
    modifier: Modifier,
    data: CharacterViewData,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = { onClick(data.id) }
    ) {
        CharacterItemImageView(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            image = data.image
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = data.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = data.species,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun CharacterItemImageView(modifier: Modifier, image: String?) {
    SubcomposeAsyncImage(
        modifier = modifier,
        model = image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
    ) {
        when (painter.state) {
            AsyncImagePainter.State.Empty -> {
                // Do nothing
            }

            is AsyncImagePainter.State.Error -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                        .padding(Spacing.small),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(R.string.generic_image_not_available),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                    )
                }
            }

            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(Spacing.extraLarge))
                }
            }

            is AsyncImagePainter.State.Success -> {
                SubcomposeAsyncImageContent(
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}