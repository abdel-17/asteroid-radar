package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.respository.AsteroidRepository
import kotlinx.coroutines.launch

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
    val repository = AsteroidRepository(
        asteroidDao = AsteroidDatabase.getInstance(application).dao
    )

    /**
     * The backing property for [errorMessage]
     * for sending error messages.
     */
    private val _errorMessage = MutableLiveData<String?>()

    /**
     * Error messages are sent through this live data.
     *
     * Call [onReceivedErrorMessage] after receiving
     * a message from this live data.
     */
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    init {
        viewModelScope.launch {
            try {
                repository.refreshData()
            } catch (e: Exception) {
                _errorMessage.value = "Connection error: ${e.message}"
            }
        }
    }

    /**
     * Resets the value of [errorMessage] to null.
     *
     * Must be called after receiving the error
     * to prevent it from being sent again
     * on configuration changes.
     */
    fun onReceivedErrorMessage() {
        _errorMessage.value = null
    }
}