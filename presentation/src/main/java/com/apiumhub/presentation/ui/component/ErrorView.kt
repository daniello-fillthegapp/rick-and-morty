package com.apiumhub.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.apiumhub.presentation.R
import com.apiumhub.presentation.ui.theme.Spacing

@Composable
fun ErrorView(
    onRetryClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.medium),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.generic_error_title),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(Spacing.extraLarge))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onRetryClicked,
        ) {
            Text(text = stringResource(R.string.generic_error_retry))
        }
    }
}