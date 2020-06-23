package robin.scaffold.jet.utils

import androidx.room.TypeConverter

object ListTypeConverters {

    @TypeConverter
    @JvmStatic
    fun stringToStrList(data: String?): List<String>? {
        if(data.isNullOrEmpty()) {
            return emptyList()
        }
        return data.split(",")
    }

    @TypeConverter
    @JvmStatic
    fun strListToString(list: List<String>?): String? {
        if(list.isNullOrEmpty()) {
            return ""
        }
        return list.joinToString(",")
    }
}