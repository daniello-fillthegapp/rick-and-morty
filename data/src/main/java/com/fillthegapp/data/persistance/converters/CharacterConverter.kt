package com.fillthegapp.data.persistance.converters

import androidx.room.TypeConverter

class CharacterConverter {
    @TypeConverter
    fun fromEpisodeList(episodeList: List<String>?): String {
        return episodeList?.joinToString(",") ?: ""
    }

    @TypeConverter
    fun toEpisodeList(data: String): List<String> {
        return if (data.isEmpty()) emptyList() else data.split(",")
    }
}