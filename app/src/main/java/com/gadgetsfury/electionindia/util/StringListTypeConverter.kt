package com.gadgetsfury.electionindia.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class StringListTypeConverter {

    companion object {

        @TypeConverter
        @JvmStatic
        fun stringToSomeObjectList(data: String?): List<String>? {
            val gson = Gson()
            if (data == null) {
                return Collections.emptyList()
            }
            val listType = object : TypeToken<List<String>>() {

            }.type
            return gson.fromJson(data, listType)
        }

        @TypeConverter
        @JvmStatic
        fun someObjectListToString(myObjects: List<String>?): String? {
            val gson = Gson()
            return gson.toJson(myObjects)
        }

    }

}