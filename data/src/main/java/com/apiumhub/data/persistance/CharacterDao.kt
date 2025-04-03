package com.apiumhub.data.persistance

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.apiumhub.data.model.entity.CharacterEntity

@Dao
interface CharacterDao {
    @Query("SELECT * FROM characters LIMIT :limit OFFSET :offset")
    suspend fun getCharactersPage(limit: Int, offset: Int): List<CharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters WHERE id = :id LIMIT 1")
    suspend fun getCharacter(id: Int): CharacterEntity?

    @Query("DELETE FROM characters")
    suspend fun clearCharacters()
}