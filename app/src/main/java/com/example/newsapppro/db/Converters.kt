package com.example.newsapppro.db

import androidx.room.TypeConverter
import com.example.newsapppro.models.Source

class Converters {
    // Store only Source.name (string) in DB; handle nulls safely
    @TypeConverter
    fun fromSource(source: Source?): String? = source?.name

    @TypeConverter
    fun toSource(name: String?): Source? = name?.let { Source(id = null, name = it) }
}
