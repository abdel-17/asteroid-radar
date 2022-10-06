package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.ImageOfTheDay
import com.udacity.asteroidradar.respository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        /**
         * @param application An instance of the application.
         *
         * @return A factory for initializing this view model.
         *
         * ViewModel objects should never be manually initialized.
         * The factory class manages creating and saving the
         * view model instances.
         */
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
     * The filter applied on the stored asteroid list.
     */
    private val _filter = MutableLiveData<AsteroidFilter?>(null)

    /**
     * The list of shown asteroids.
     *
     * The value of this property is updated
     * when new data is fetched or a new filter
     * is applied with [applyFilter].
     */
    val asteroids = repository.asteroids.switchMap { asteroids ->
        _filter.map { filter ->
            // If the filter is `null`, the original list is returned.
            filter?.appliedOn(asteroids) ?: asteroids
        }
    }

    /**
     * The image of the day fetched from the network.
     */
    val imageOfTheDay: LiveData<ImageOfTheDay>
        get() = repository.imageOfTheDay

//    /**
//     * The backing property for [errorMessage]
//     * for sending error messages.
//     */
//    private val _errorMessage = MutableLiveData<String?>()
//
//    /**
//     * Error messages are sent through this live data.
//     *
//     * Call [onReceivedErrorMessage] after receiving
//     * a message from this live data.
//     */
//    val errorMessage: LiveData<String?>
//        get() = _errorMessage

    init {
        viewModelScope.launch {
            try {
                repository.refreshImageOfTheDay()
                repository.refreshAsteroids()
            } catch (e: Exception) {
//                _errorMessage.value = "Connection error: ${e.message}"
                Log.i("ViewModel", "Connection error: ${e.message}")
            }
        }
    }

    /**
     * Applies [filter] on the asteroid list,
     * showing the entire list if it's `null`.
     */
    fun applyFilter(filter: AsteroidFilter?) {
        _filter.value = filter
    }

//    /**
//     * Resets the value of [errorMessage] to null.
//     *
//     * Must be called after receiving the error
//     * to prevent it from being sent again
//     * on configuration changes.
//     */
//    fun onReceivedErrorMessage() {
//        _errorMessage.value = null
//    }
}