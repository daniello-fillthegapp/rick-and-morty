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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.fillthegapp.presentation.viewmodel.CharacterListScreenAction
import com.fillthegapp.presentation.viewmodel.CharactersScreenState
import com.fillthegapp.presentation.viewmodel.CharactersViewModel

@Composable
fun CharactersScreen(
    onNavigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharactersViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsState()
    val networkState by viewModel.networkState.collectAsState()

    LaunchedEffect(true) {
        viewModel.screenAction.collect { action ->
            when (action) {
                is CharacterListScreenAction.NavigateToCharacterDetailAction -> {
                    onNavigateToDetails(action.characterId)
                }
            }
        }
    }

    CharactersScreenContent(
        modifier = modifier,
        state = screenState,
        isNetworkAvailable = networkState,
        onRetryClicked = viewModel::onRetryClicked,
        onMoreItemsRequested = viewModel::onMoreItemsRequested,
        onCharacterClicked = viewModel::onCharacterClicked
    )
}

@Composable
private fun CharactersScreenContent(
    onRetryClicked: () -> Unit,
    onMoreItemsRequested: () -> Unit,
    onCharacterClicked: (Int) -> Unit,
    modifier: Modifier,
    state: CharactersScreenState,
    isNetworkAvailable: Boolean,
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
                    isErrorLoadingMore = currentState.isErrorLoadingMore,
                    onMoreItemsRequested = onMoreItemsRequested,
                    onCharacterClicked = onCharacterClicked
                )
            }
        }

        if (!isNetworkAvailable) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "You're offline",
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.bodyMedium
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
    isErrorLoadingMore: Boolean,
    data: PaginatedCharacterListViewData,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize()
    ) {
        itemsIndexed(data.items) { index, item ->
            Spacer(modifier = Modifier.height(Spacing.large))

            CharacterItemView(
                modifier = Modifier.padding(
                    horizontal = Spacing.medium,
                    vertical = Spacing.small
                ),
                data = item,
                onClick = onCharacterClicked
            )

            if (index >= data.items.size - 2) {
                LaunchedEffect(Unit) {
                    onMoreItemsRequested()
                }
            }
        }

        if (isErrorLoadingMore) {
            item {
                Text(
                    modifier = Modifier.fillMaxWidth().padding(vertical = Spacing.large),
                    text = stringResource(R.string.offline_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
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
                text = data.locationName,
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