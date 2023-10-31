package com.example.internshalaassignment.models

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson

import java.time.LocalDate
import java.util.Date
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString

class Converters {


    @TypeConverter
    fun fromLocalDatetoString(date: LocalDate): String = date.toString()

    @TypeConverter
    fun fromStringToLocalDate(string: String): LocalDate = LocalDate.parse(string)

//    @TypeConverter
//    fun fromUserToString(user: User): String = Gson().toJson(user)
//
//    @TypeConverter
//    fun fromStringToUser(string: String): User = Gson().fromJson(string, User::class.java)

    @TypeConverter
    fun stringToList(string: String): MutableList<Int> = Json.decodeFromString(string)

    @TypeConverter
    fun listToString(list: MutableList<Int>): String = Json.encodeToString(list)
}