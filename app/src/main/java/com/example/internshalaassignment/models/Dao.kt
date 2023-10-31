package com.example.internshalaassignment.models

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface Dao {

    @Upsert
    suspend fun upsertWorkshop(workshop: Workshop)

    @Query("Select * FROM workshop")
    suspend fun getWorkshops(): List<Workshop>

    @Upsert
    suspend fun upsertUser(user: User)

    @Query("Select * from User")
    suspend fun getUsers(): List<User>

    @Query("Select * from User where username = :username")
    suspend fun getUser(username: String): User?
}