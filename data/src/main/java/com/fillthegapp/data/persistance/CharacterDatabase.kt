package com.fillthegapp.data.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fillthegapp.data.model.entity.CharacterEntity
import com.fillthegapp.data.persistance.converters.CharacterConverter

@Database(entities = [CharacterEntity::class], version = 1)
@TypeConverters(CharacterConverter::class)
abstract class CharacterDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}