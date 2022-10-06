package com.udacity.asteroidradar.work

import android.app.Application
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.respository.AsteroidRepository
import java.text.SimpleDateFormat
import java.util.*

class RefreshDataWorker(
    application: Application,
    parameters: WorkerParameters
) : CoroutineWorker(application, parameters) {
    companion object {
        const val NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val dao = AsteroidDatabase.getInstance(applicationContext).dao
        val repository = AsteroidRepository(dao)
        return try {
            repository.refreshAsteroids()
            // Delete the stored asteroids before today.
            val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
            val today = dateFormat.format(Calendar.getInstance().time)
            dao.deleteAsteroidsBeforeDate(today)
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}