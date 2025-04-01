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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.fillthegapp.presentation.R
import com.fillthegapp.presentation.model.CharacterViewData
import com.fillthegapp.presentation.ui.component.ErrorView
import com.fillthegapp.presentation.ui.component.LoaderView
import com.fillthegapp.presentation.ui.theme.Spacing
import com.fillthegapp.presentation.viewmodel.CharacterDetailScreenAction
import com.fillthegapp.presentation.viewmodel.CharacterDetailScreenState
import com.fillthegapp.presentation.viewmodel.CharacterDetailScreenState.Loaded
import com.fillthegapp.presentation.viewmodel.CharacterDetailScreenState.Loading
import com.fillthegapp.presentation.viewmodel.CharacterDetailViewModel

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
    Column(modifier = modifier.fillMaxSize()) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null,
                    )
                }
            }
        )
        Box(modifier = modifier.fillMaxSize()) {
            when (state) {
                Loading -> {
                    LoaderView()
                }

                CharacterDetailScreenState.Error -> {
                    ErrorView(
                        onRetryClicked = onRetryClicked,
                    )
                }

                is Loaded -> {
                    CharacterDetailsView(data = state.data)
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailsView(
    modifier: Modifier = Modifier,
    data: CharacterViewData,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        CharacterImageView(
            modifier = Modifier.fillMaxWidth(),
            image = data.image,
        )
        Spacer(modifier = Modifier.height(Spacing.medium))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.medium)
        ) {
            Text(
                text = data.name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = data.species,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = data.gender,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
            Text(
                text = data.status,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
            if (data.type.isNotEmpty()) {
                Text(
                    text = data.type,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
            Text(
                text = data.locationName,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

@Composable
private fun CharacterImageView(
    modifier: Modifier = Modifier,
    image: String?,
) {
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