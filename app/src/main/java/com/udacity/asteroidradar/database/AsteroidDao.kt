package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    /**
     * @return The list of asteroids stored in the database
     * ordered by their `closeApproachDate` property.
     */
    @Query("SELECT * FROM Asteroid ORDER BY closeApproachDate DESC")
    fun getAsteroids(): LiveData<List<Asteroid>>

    /**
     * Inserts the given list of asteroids in the database,
     * replacing values with the same id.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<Asteroid>)
}