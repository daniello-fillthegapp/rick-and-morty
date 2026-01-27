package com.apiumhub.presentation.widget

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.ActionParameters
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.currentState
import androidx.glance.layout.Box
import androidx.glance.layout.fillMaxSize
import androidx.glance.text.Text
import dagger.hilt.android.EntryPointAccessors

object WidgetPrefs {
    val characterImage = stringPreferencesKey("characterImage")
}

class RickAndMortyWidget : GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {
        provideContent {
            val state = currentState<Preferences>()
            val currentCharacter = state[WidgetPrefs.characterImage]
            WidgetContent(currentCharacter)
        }
    }
}

@Composable
private fun WidgetContent(
    characterImage: String?
) {
    Box(
        modifier = GlanceModifier.clickable(actionRunCallback<WidgetCallback>())
    ) {
        if (characterImage != null) {//Warning: all images shall be locally stored first
            val bitmap = BitmapFactory.decodeFile(characterImage)
            Image(
                modifier = GlanceModifier.fillMaxSize(),
                provider = ImageProvider(bitmap),
                contentDescription = null,
            )
        } else {
            Text("No image available")
        }
    }
}

class WidgetCallback : ActionCallback { //Warning: not private
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        try {
            val appContext = context.applicationContext
            val entryPoint = EntryPointAccessors.fromApplication(
                appContext,
                WidgetEntryPoint::class.java
            )

            val useCase = entryPoint.getCharactersPageUseCase()
            val items = useCase.execute(index = 1).getOrThrow().items.map { it.image }
            val randomIndex = (items.indices).random()
            val result = items[randomIndex]

            updateAppWidgetState(context, glanceId) { pref: MutablePreferences ->
                pref[WidgetPrefs.characterImage] = result
            }

            RickAndMortyWidget().update(context, glanceId)
        } catch (e: Exception) {
            Log.e("RickAndMortyWidget", "error: ${e.message}")
        }
    }
}