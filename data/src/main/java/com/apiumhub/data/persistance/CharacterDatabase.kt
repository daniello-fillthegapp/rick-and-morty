package com.apiumhub.data.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.apiumhub.data.model.entity.CharacterEntity
import com.apiumhub.data.persistance.converters.CharacterConverter

@Database(entities = [CharacterEntity::class], version = 1)
@TypeConverters(CharacterConverter::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}