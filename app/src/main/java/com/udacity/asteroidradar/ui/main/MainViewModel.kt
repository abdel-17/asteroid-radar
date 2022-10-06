package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.respository.AsteroidRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
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
     * for emitting error messages.
     */
    private val _errorMessage = MutableSharedFlow<String>()

    /**
     * Error messages are emitted through this flow.
     */
    val errorMessage: SharedFlow<String>
        get() = _errorMessage

    init {
        viewModelScope.launch {
            try {
                repository.refreshData()
            } catch (e: Exception) {
                _errorMessage.emit("Connection error: ${e.message}")
            }
        }
    }
}