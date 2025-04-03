package com.fillthegapp.data.datasource

import android.content.Context
import android.util.Log
import com.fillthegapp.data.model.entity.CharacterEntity
import com.fillthegapp.data.persistance.CharacterDao
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val characterDao: CharacterDao,
    @ApplicationContext private val context: Context,
) {
    suspend fun getCharactersPage(pageIndex: Int, pageSize: Int = 19): List<CharacterEntity> {
        val offset = pageIndex * pageSize
        val charactersPage = characterDao.getCharactersPage(limit = pageSize, offset = offset)
        return charactersPage
    }

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        characterDao.insertCharacters(characters)
    }

    suspend fun getCharacter(id: Int): CharacterEntity? {
        return characterDao.getCharacter(id)
    }

    suspend fun clearCharacters() {
        characterDao.clearCharacters()
    }

    fun storeMediaImage(
        imageUrl: String,
        imageData: ByteArray
    ): String? {
        return try {
            val fileName = imageUrl.substringAfterLast("/")
            val directory = File(context.filesDir, "images")
            directory.mkdirs()

            val file = File(directory, fileName)
            file.writeBytes(imageData)
            file.absolutePath
        } catch (e: Exception) {
            Log.e(this.javaClass.name, e.message, e)
            null
        }
    }
}