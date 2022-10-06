package com.udacity.asteroidradar.respository

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.network.ImageOfTheDay
import com.udacity.asteroidradar.network.Network
import com.udacity.asteroidradar.network.parseAsteroidsJSONResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(val asteroidDao: AsteroidDao) {
    /**
     * Fetches the asteroids from the network,
     * updating the cache in the process.
     */
    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            // Format the date today to pass as a query parameter.
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val today = dateFormat.format(Calendar.getInstance().time)
            val asteroidsJSON = Network.nasaService.getAsteroidsJSON(startDate = today)
            val asteroidsList = parseAsteroidsJSONResult(asteroidsJSON)
            asteroidDao.insertAll(asteroidsList)
        }
    }

    /**
     * @return The latest image of the day
     * fetched from the network.
     */
    suspend fun fetchImageOfTheDay(): ImageOfTheDay {
         return withContext(Dispatchers.IO) {
            Network.nasaService.getImageOfTheDay()
        }
    }
}