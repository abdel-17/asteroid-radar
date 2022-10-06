package com.udacity.asteroidradar.ui.main

import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.Asteroid
import java.text.SimpleDateFormat
import java.util.*

/**
 * An enum of filters for showing only a
 * subset of the stored asteroids.
 */
enum class AsteroidFilter {
    TODAY, THIS_WEEK;

    /**
     * @return [asteroids] with this filter applied.
     */
    fun appliedOn(asteroids: List<Asteroid>): List<Asteroid> {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val calender = Calendar.getInstance()
        return asteroids.filter { asteroid ->
            when (this) {
                TODAY -> {
                    val today = dateFormat.format(calender.time)
                    asteroid.closeApproachDate == today
                }
                THIS_WEEK -> {
                    // Set the calender to the first day of the week.
                    calender.set(Calendar.DAY_OF_WEEK, calender.firstDayOfWeek)
                    val weekStart = dateFormat.format(calender.time)
                    // Set the calender to the last day of the week.
                    calender.set(Calendar.DAY_OF_WEEK, 7)
                    val weekEnd = dateFormat.format(calender.time)
                    // Choose asteroid whose dates lie between
                    // this weeks's start and end.
                    asteroid.closeApproachDate.let { date ->
                        weekStart <= date && date <= weekEnd
                    }
                }
            }
        }
    }
}