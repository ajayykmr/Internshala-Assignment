package com.example.internshalaassignment.models

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Workshop::class, User::class],
    version = 1
)

@TypeConverters(Converters::class)
abstract class LocalDatabase: RoomDatabase() {
    abstract fun dao(): Dao

    companion object {

        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getDatabase(context: Context): LocalDatabase{
            if (INSTANCE==null){
            synchronized(this) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        LocalDatabase::class.java,
                        "localDB"
                    ).build()
                }
            }

            return INSTANCE!!
        }
    }
}