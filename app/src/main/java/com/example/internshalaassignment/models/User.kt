package com.example.internshalaassignment.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity
data class User(
    @PrimaryKey(autoGenerate = false)
    val username: String,
    val password: String,

    var appliedWorkshopIDs: MutableList<Int> = mutableListOf(),

)
