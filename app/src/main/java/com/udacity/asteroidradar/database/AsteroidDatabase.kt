package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val dao: AsteroidDao

    companion object {
        private lateinit var INSTANCE: AsteroidDatabase

        fun getInstance(context: Context): AsteroidDatabase {
            // Creating a database is expensive, so we keep a single instance.
            if (this::INSTANCE.isInitialized) return INSTANCE
            // Initialize the instance, applying a lock to prevent
            // multiple threads from accessing `INSTANCE`.
            synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AsteroidDatabase::class.java,
                    "asteroids"
                ).build()
                return INSTANCE
            }
        }
    }
}