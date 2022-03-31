package com.software.hemis.database.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.reflect.TypeToken

class Converter {
    @TypeConverter
    fun toHashMap(value: String): HashMap<String, String> =
        Gson().fromJson(value, object : TypeToken<HashMap<String, String>>() {}.type)

    @TypeConverter
    fun fromHashMap(value: HashMap<String, String>): String =
        Gson().toJson(value)
}