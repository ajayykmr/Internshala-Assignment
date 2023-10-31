package com.example.internshalaassignment.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.Date

@Entity
data class Workshop(
    val workshopName: String,
    val location: String,
    var totalSeats: Int,
    var registeredSeats: Int,
    val workshopDate: LocalDate,
    @PrimaryKey(autoGenerate = true)
    val workshopID: Int = 0,
)
