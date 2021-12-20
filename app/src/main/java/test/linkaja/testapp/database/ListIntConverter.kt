package test.linkaja.testapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ListIntConverter {
    @TypeConverter
    fun fromInt(value: String?): List<Int?>? {
        val listType: Type = object : TypeToken<List<Int?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<Int?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}