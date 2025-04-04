package com.apiumhub.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.apiumhub.presentation.R
import com.apiumhub.presentation.model.CharacterStatus
import com.apiumhub.presentation.model.CharacterViewData
import com.apiumhub.presentation.model.PaginatedCharacterListViewData
import com.apiumhub.presentation.ui.component.ErrorView
import com.apiumhub.presentation.ui.component.LoaderView
import com.apiumhub.presentation.ui.theme.Shapes
import com.apiumhub.presentation.ui.theme.Spacing
import com.apiumhub.presentation.viewmodel.CharacterListScreenAction
import com.apiumhub.presentation.viewmodel.CharactersScreenState
import com.apiumhub.presentation.viewmodel.CharactersViewModel

@Composable
fun CharactersScreen(
    onNavigateToDetails: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharactersViewModel = hiltViewModel(),
) {
    val screenState by viewModel.screenState.collectAsState()
    val isNetworkAvailableState by viewModel.isNetworkAvailableState.collectAsState()

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
        isNetworkAvailable = isNetworkAvailableState,
        onRetryClicked = viewModel::onRetryClicked,
        onMoreItemsRequested = viewModel::onMoreItemsRequested,
        onCharacterClicked = viewModel::onCharacterClicked,
    )
}

@Composable
private fun CharactersScreenContent(
    onRetryClicked: () -> Unit,
    onMoreItemsRequested: () -> Unit,
    onCharacterClicked: (Int) -> Unit,
    state: CharactersScreenState,
    isNetworkAvailable: Boolean,
    modifier: Modifier = Modifier,
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
                    onMoreItemsRequested = onMoreItemsRequested,
                    onCharacterClicked = onCharacterClicked,
                    isErrorLoadingMore = currentState.isErrorLoadingMore,
                    isNetworkAvailable = isNetworkAvailable,
                    data = currentState.data,
                    isLoadingMoreData = currentState.isLoadingMoreData
                )
            }
        }

        if (!isNetworkAvailable) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.error)
                    .padding(Spacing.small),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.offline_banner),
                    color = MaterialTheme.colorScheme.onError,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterListView(
    onMoreItemsRequested: () -> Unit,
    onCharacterClicked: (Int) -> Unit,
    isErrorLoadingMore: Boolean,
    isLoadingMoreData: Boolean,
    isNetworkAvailable: Boolean,
    data: PaginatedCharacterListViewData,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(vertical = Spacing.large)
    ) {
        itemsIndexed(data.items) { index, item ->
            CharacterItemView(
                modifier = Modifier.padding(
                    start = Spacing.medium,
                    end = Spacing.medium,
                    bottom = Spacing.medium
                ),
                data = item,
                onClick = onCharacterClicked
            )

            //start loading before user reaches the image.
            if (index > data.items.size - AVERAGE_ITEMS_DISPLAYED) {
                LaunchedEffect(Unit) {
                    onMoreItemsRequested()
                }
            }
        }

        if (isLoadingMoreData) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = Spacing.medium),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(Spacing.extraLarge))
                }
            }
        }

        if (isNetworkAvailable && isErrorLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = Spacing.medium),
                    contentAlignment = Alignment.Center,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(Shapes.large)
                            .clickable { onMoreItemsRequested() }) {
                        Text(
                            modifier = Modifier.padding(Spacing.small),
                            text = stringResource(R.string.click_to_load_more_content),
                            style = MaterialTheme.typography.titleSmall,
                        )
                        IconButton(
                            modifier = Modifier.size(Spacing.extraLarge),
                            onClick = {},
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = null,
                            )
                        }
                    }

                }
            }
        }

        if (!isNetworkAvailable) {
            item {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = Spacing.large),
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
    data: CharacterViewData,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(8.dp, Shapes.large)
            .clip(Shapes.large)
            .clickable { onClick(data.id) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(Shapes.large)
        ) {
            CharacterItemImageView(
                modifier = Modifier.fillMaxSize(),
                image = data.image
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        ),
                    ),
                    shape = Shapes.large
                )
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.medium)
                .align(Alignment.BottomStart)
        ) {
            Text(
                text = data.name,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.surface
            )

            Text(
                text = data.locationName,
                style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun CharacterItemImageView(image: String?, modifier: Modifier = Modifier) {
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
                    modifier = Modifier.fillMaxSize(),
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

private const val AVERAGE_ITEMS_DISPLAYED = 5

@Preview(showBackground = true)
@Composable
fun PreviewCharacterItemView() {
    val testData = CharacterViewData(
        id = 1,
        name = "Rick Sanchez",
        locationName = "Earth",
        image = "",
        species = "",
        type = "",
        gender = "",
        creationDate = "",
        originName = "",
        episodesLabel = "",
        episodesAmount = 1,
        status = CharacterStatus.ALIVE
    )

    CharacterItemView(
        onClick = {},
        modifier = Modifier.padding(16.dp),
        data = testData
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCharactersScreenOnError() {
    CharactersScreenContent(
        modifier = Modifier,
        state = CharactersScreenState.Error,
        isNetworkAvailable = true,
        onRetryClicked = {},
        onMoreItemsRequested = {},
        onCharacterClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCharactersScreenOnLoading() {
    CharactersScreenContent(
        modifier = Modifier,
        state = CharactersScreenState.Loading,
        isNetworkAvailable = true,
        onRetryClicked = {},
        onMoreItemsRequested = {},
        onCharacterClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCharactersScreenOnLoaded() {
    CharactersScreenContent(
        modifier = Modifier,
        state = CharactersScreenState.Loaded(
            isLoadingMoreData = false,
            isErrorLoadingMore = false,
            data = PaginatedCharacterListViewData(
                hasMoreItems = true,
                nextPageIndex = 2,
                items = listOf(
                    CharacterViewData(
                        name = "Rick Sanchez",
                        species = "Human",
                        gender = "Male",
                        status = CharacterStatus.DEAD,
                        type = "",
                        locationName = "Earth",
                        episodesAmount = 9,
                        episodesLabel = "1, 2, 3, 4, 5, 6, 7, 8, 9",
                        image = "",
                        id = 0,
                        creationDate = "",
                        originName = "Earth"
                    )
                )
            )
        ),
        isNetworkAvailable = true,
        onRetryClicked = {},
        onMoreItemsRequested = {},
        onCharacterClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCharactersScreenOnLoadedOffline() {
    CharactersScreenContent(
        modifier = Modifier,
        state = CharactersScreenState.Loaded(
            isLoadingMoreData = false,
            isErrorLoadingMore = false,
            data = PaginatedCharacterListViewData(
                hasMoreItems = true,
                nextPageIndex = 2,
                items = listOf(
                    CharacterViewData(
                        name = "Rick Sanchez",
                        species = "Human",
                        gender = "Male",
                        status = CharacterStatus.DEAD,
                        type = "",
                        locationName = "Earth",
                        episodesAmount = 9,
                        episodesLabel = "1, 2, 3, 4, 5, 6, 7, 8, 9",
                        image = "",
                        id = 0,
                        creationDate = "",
                        originName = "Earth"
                    )
                )
            )
        ),
        isNetworkAvailable = false,
        onRetryClicked = {},
        onMoreItemsRequested = {},
        onCharacterClicked = {}
    )
}