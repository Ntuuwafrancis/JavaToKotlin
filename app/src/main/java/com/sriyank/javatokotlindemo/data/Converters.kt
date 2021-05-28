package com.sriyank.javatokotlindemo.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sriyank.javatokotlindemo.models.Owner

object Converters {
    //    @TypeConverter
    //    public static Date fromTimestamp(Long value) {
    //        return value == null ? null : new Date(value);
    //    }
    //
    //    @TypeConverter
    //    public static Long dateToTimestamp(Date date) {
    //        return date == null ? null : date.getTime();
    //    }
    @JvmStatic
    @TypeConverter
    fun fromStringToOwner(value: String?): Owner {
        val owner = object : TypeToken<Owner?>() {}.type
        return Gson().fromJson(value, owner)
    }

    @JvmStatic
    @TypeConverter
    fun ownerToString(owner: Owner?): String {
        val gson = Gson()
        return gson.toJson(owner)
    }
}