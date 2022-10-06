package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AsteroidDao {
    /**
     * @param date The date from which asteroids are fetched.
     *
     * @return The list of asteroids stored in the database
     * ordered by their `closeApproachDate` property.
     */
    @Query("SELECT * FROM Asteroid WHERE closeApproachDate >= :date ORDER BY closeApproachDate DESC")
    fun getAsteroidsFromDate(date: String): LiveData<List<Asteroid>>

    /**
     * Inserts the given list of asteroids in the database,
     * replacing values with the same id.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(asteroids: List<Asteroid>)

    @Query("DELETE FROM Asteroid WHERE closeApproachDate < :date")
    suspend fun deleteAsteroidsBeforeDate(date: String)
}