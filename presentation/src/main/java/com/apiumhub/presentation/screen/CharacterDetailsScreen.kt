package com.apiumhub.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.apiumhub.presentation.R
import com.apiumhub.presentation.model.CharacterStatus
import com.apiumhub.presentation.model.CharacterViewData
import com.apiumhub.presentation.ui.component.ErrorView
import com.apiumhub.presentation.ui.component.LoaderView
import com.apiumhub.presentation.ui.theme.ErrorRed
import com.apiumhub.presentation.ui.theme.Spacing
import com.apiumhub.presentation.ui.theme.SuccessGreen
import com.apiumhub.presentation.viewmodel.CharacterDetailScreenAction
import com.apiumhub.presentation.viewmodel.CharacterDetailScreenState
import com.apiumhub.presentation.viewmodel.CharacterDetailScreenState.Loaded
import com.apiumhub.presentation.viewmodel.CharacterDetailScreenState.Loading
import com.apiumhub.presentation.viewmodel.CharacterDetailViewModel

@Composable
fun CharacterDetailsScreen(
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CharacterDetailViewModel = hiltViewModel()
) {
    val screenState by viewModel.screenState.collectAsState()

    LaunchedEffect(true) {
        viewModel.screenAction.collect { action ->
            when (action) {
                CharacterDetailScreenAction.NavigateBackAction -> onNavigateBack()
            }
        }
    }

    CharacterDetailScreenContent(
        modifier = modifier,
        state = screenState,
        onRetryClicked = viewModel::onRetryClicked,
        onBackClicked = viewModel::onNavigateBackClicked
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailScreenContent(
    onRetryClicked: () -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier,
    state: CharacterDetailScreenState,
) {
    Box(modifier = modifier.fillMaxSize()) {
        when (state) {
            Loading -> {
                CharacterDetailsLoadingView(modifier = modifier, onBackClicked = onBackClicked)
            }

            CharacterDetailScreenState.Error -> {
                CharacterDetailsErrorView(
                    modifier = modifier,
                    onBackClicked = onBackClicked,
                    onRetryClicked = onRetryClicked
                )

            }

            is Loaded -> {
                CharacterDetailsView(data = state.data, onBackClicked = onBackClicked)
            }
        }
    }
}

@Composable
private fun CharacterDetailsLoadingView(
    modifier: Modifier,
    onBackClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.extraSmall)
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null,
            )
        }
        LoaderView()
    }
}

@Composable
private fun CharacterDetailsErrorView(
    modifier: Modifier,
    onBackClicked: () -> Unit,
    onRetryClicked: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.extraSmall)
    ) {
        IconButton(onClick = onBackClicked) {
            Icon(
                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = null,
            )
        }
        ErrorView(
            onRetryClicked = onRetryClicked,
        )
    }
}

@Composable
fun CharacterDetailsView(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    data: CharacterViewData,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        AsyncImage(
            model = data.image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.2f)
        )

        Column {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(Spacing.extraSmall)
            ) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(Spacing.medium),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    elevation = CardDefaults.cardElevation(defaultElevation = DEFAULT_ELEVATION.dp),
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    AsyncImage(
                        model = data.image,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(Spacing.medium))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = DEFAULT_ELEVATION.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(Spacing.medium)
                    ) {
                        Text(
                            text = data.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(Spacing.small))

                        CharacterDetailItem(
                            title = stringResource(R.string.character_description_species),
                            value = data.species
                        )

                        CharacterDetailItem(
                            title = stringResource(R.string.character_description_gender),
                            value = data.gender
                        )

                        CharacterStatusDetailItem(status = data.status)

                        if (data.type.isNotEmpty()) {
                            CharacterDetailItem(
                                title = stringResource(R.string.character_description_type),
                                value = data.type
                            )
                        }

                        CharacterDetailItem(
                            title = stringResource(R.string.character_description_location),
                            value = data.locationName
                        )

                        if (!data.shouldShowEpisodeTab) {
                            CharacterDetailItem(
                                title = pluralStringResource(
                                    R.plurals.episode,
                                    data.episodesAmount
                                ),
                                value = data.episodesLabel
                            )
                        }
                    }
                }

                if (data.shouldShowEpisodeTab) {
                    Spacer(modifier = Modifier.height(Spacing.medium))

                    CharacterEpisodesView(
                        value = data.episodesLabel
                    )
                }
            }
        }

    }
}

@Composable
fun CharacterDetailItem(title: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.extraSmall),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            maxLines = 1,
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.width(Spacing.medium))

        Text(
            modifier = Modifier.weight(1f),
            text = value,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun CharacterStatusDetailItem(status: CharacterStatus) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = Spacing.extraSmall),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            maxLines = 1,
            text = stringResource(R.string.character_description_status),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Spacer(modifier = Modifier.width(Spacing.medium))

        Text(
            modifier = Modifier.weight(1f),
            text = status.status,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyLarge,
            color = when (status) {
                CharacterStatus.ALIVE -> SuccessGreen
                CharacterStatus.DEAD -> ErrorRed
                else -> MaterialTheme.colorScheme.onSurface
            }
        )
    }
}

@Composable
fun CharacterEpisodesView(value: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = DEFAULT_ELEVATION.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(
            modifier = Modifier.padding(
                start = Spacing.medium,
                top = Spacing.medium,
                bottom = Spacing.medium
            )
        ) {
            Text(
                maxLines = 1,
                text = stringResource(R.string.episodes),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(Spacing.medium))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Text(
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .padding(end = Spacing.medium),
                    text = value,
                    maxLines = 1,
                    overflow = TextOverflow.Visible,
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                listOf(
                                    Color.Transparent,
                                    MaterialTheme.colorScheme.surface
                                ),
                                startX = 800f,
                            )
                        )
                )
            }
        }
    }
}

private const val DEFAULT_ELEVATION = 4

@Preview(showBackground = true)
@Composable
fun PreviewCharacterDetailsLoading() {
    CharacterDetailsLoadingView(
        modifier = Modifier.fillMaxSize(),
        onBackClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCharacterDetailsError() {
    CharacterDetailsErrorView(
        modifier = Modifier.fillMaxSize(),
        onBackClicked = {},
        onRetryClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCharacterDetailsLoaded() {
    CharacterDetailsView(
        data = CharacterViewData(
            name = "Rick Sanchez",
            species = "Human",
            gender = "Male",
            status = CharacterStatus.ALIVE,
            type = "",
            locationName = "Earth",
            episodesAmount = 10,
            episodesLabel = "1, 2, 3, 4, 5, 6, 7, 8, 9, 10",
            image = "",
            id = 0,
            creationDate = "",
            originName = "Earth"
        ),
        onBackClicked = {}
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCharacterDetailsLoadedLessThan10EpisodesDead() {
    CharacterDetailsView(
        data = CharacterViewData(
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
        ),
        onBackClicked = {}
    )
}