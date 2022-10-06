package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.udacity.asteroidradar.network.*
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.respository.AsteroidRepository
import kotlinx.coroutines.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        fun provideFactory(application: Application) = viewModelFactory {
            initializer {
                MainViewModel(application)
            }
        }
    }

    /**
     * The repository used to fetch and cache data.
     */
    private val repository = AsteroidRepository(
        asteroidDao = AsteroidDatabase.getInstance(application).dao
    )

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
     * The current list of asteroids stored in the database.
     */
    val asteroidList: LiveData<List<Asteroid>> by lazy {
        repository.asteroidDao.getAsteroids()
    }

    init {
        refreshData()
    }

    private fun refreshData() {
        viewModelScope.launch {
            repository.refreshAsteroids()
            // todo fetch image
        }
    }
}