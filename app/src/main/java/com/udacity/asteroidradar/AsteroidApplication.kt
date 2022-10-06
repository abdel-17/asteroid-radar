package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication : Application() {
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            setupRefreshWork()
        }
    }

    private fun setupRefreshWork() {
        // Only refresh when the following constraints are met:
        // 1) The device is on Wifi.
        // 2) The device is charging.
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresCharging(true)
            .build()
        // Repeat the cache refresh once every day.
        val refreshRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            repeatInterval = 1,
            repeatIntervalTimeUnit = TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            RefreshDataWorker.NAME,
            // If two requests have the same name, the old one continues.
            ExistingPeriodicWorkPolicy.KEEP,
            refreshRequest
        )
    }
}