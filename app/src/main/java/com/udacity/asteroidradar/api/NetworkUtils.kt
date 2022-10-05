package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.ui.Asteroid
import com.udacity.asteroidradar.Constants
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun parseAsteroidsJSONResult(resultJSON: JSONObject): List<Asteroid> = buildList {
    val nearEarthObjectsJSON = resultJSON.getJSONObject("near_earth_objects")
    for (formattedDate in getNextSevenDaysFormattedDates()) {
        try {
            val dateAsteroidJSONArray = nearEarthObjectsJSON.getJSONArray(formattedDate)
            for (i in 0 until dateAsteroidJSONArray.length()) {
                val asteroidJSON = dateAsteroidJSONArray.getJSONObject(i)
                this.add(asteroidJSON.parseAsteroid(formattedDate))
            }
        } catch (e: JSONException) {
            continue
        }
    }
}

private fun JSONObject.parseAsteroid(formattedDate: String): Asteroid {
    val closeApproachData = getJSONArray("close_approach_data")
        .getJSONObject(0)
    return Asteroid(
        id = getLong("id"),
        codename = getString("name"),
        closeApproachDate = formattedDate,
        absoluteMagnitude = getDouble("absolute_magnitude_h"),
        estimatedDiameter = getJSONObject("estimated_diameter")
            .getJSONObject("kilometers")
            .getDouble("estimated_diameter_max"),
        relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
            .getDouble("kilometers_per_second"),
        distanceFromEarth = closeApproachData.getJSONObject("miss_distance")
            .getDouble("astronomical"),
        isPotentiallyHazardous = getBoolean("is_potentially_hazardous_asteroid"),
    )
}

private fun getNextSevenDaysFormattedDates(): List<String> = buildList {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
    for (i in 0..Constants.DEFAULT_END_DATE_DAYS) {
        val currentTime = calendar.time
        this.add(dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }
}
