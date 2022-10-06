package com.udacity.asteroidradar.respository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.network.ImageOfTheDay
import com.udacity.asteroidradar.network.Network
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class AsteroidRepository(private val asteroidDao: AsteroidDao) {
    /**
     * The current list of asteroids stored in the database.
     */
    val asteroids: LiveData<List<Asteroid>> by lazy {
        asteroidDao.getAsteroids()
    }

    /**
     * The private backing property for [imageOfTheDay].
     *
     * We don't expose this property publicly to
     * encapsulate mutability, which reduces bugs.
     */
    private val _imageOfTheDay = MutableLiveData<ImageOfTheDay>()

    /**
     * The image of the day fetched from the network.
     */
    val imageOfTheDay: LiveData<ImageOfTheDay>
        get() = _imageOfTheDay

    /**
     * Updates the values of [imageOfTheDay] and
     * [asteroids] to the latest versions.
     *
     * This methods runs on the IO dispatcher
     * as it's optimized for running network
     * and disk-access related tasks.
     */
    suspend fun refreshData() {
        withContext(Dispatchers.IO) {
            refreshImageOfTheDay()
            refreshAsteroids()
        }
    }

    /**
     * Fetches the asteroids from the network,
     * updating the cache on success.
     */
    private suspend fun refreshAsteroids() {
        // Format the date today to pass as a query parameter.
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(Calendar.getInstance().time)
        // Parse the returned JSON string as a list of asteroids, then cache it.
        val asteroidsJSON = Network.nasaService.getAsteroidsJSON(startDate = today)
        val asteroidsList = parseAsteroidsJSONResult(asteroidsJSON)
        asteroidDao.insertAll(asteroidsList)
    }

    /**
     * Replaces the value of [imageOfTheDay] with
     * the latest fetched from the network.
     */
    private suspend fun refreshImageOfTheDay() {
        // We use `postValue` to set its value on the main thread.
        _imageOfTheDay.postValue(Network.nasaService.getImageOfTheDay())
    }
}