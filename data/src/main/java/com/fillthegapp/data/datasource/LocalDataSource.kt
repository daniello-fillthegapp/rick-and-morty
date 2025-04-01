package com.fillthegapp.data.datasource

import android.util.Log
import com.fillthegapp.data.model.entity.CharacterEntity
import com.fillthegapp.data.persistance.CharacterDao
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    private val characterDao: CharacterDao
) {
    suspend fun getCharactersPage(pageIndex: Int, pageSize: Int = 19): List<CharacterEntity> {
        val offset = pageIndex * pageSize
        val charactersPage = characterDao.getCharactersPage(limit = pageSize, offset = offset)
        Log.d("DatabaseTest", "Retrieving characters for page index $pageIndex: $charactersPage")
        return charactersPage
    }

    suspend fun insertCharacters(characters: List<CharacterEntity>) {
        Log.d("DatabaseTest", "Inserting characters: $characters")
        characterDao.insertCharacters(characters)
    }

    suspend fun getCharacter(id: Int): CharacterEntity? {
        return characterDao.getCharacter(id)
    }

    suspend fun clearCharacters() {
        characterDao.clearCharacters()
    }
}