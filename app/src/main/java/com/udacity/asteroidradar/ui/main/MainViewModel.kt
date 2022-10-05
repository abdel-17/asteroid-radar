package com.udacity.asteroidradar.ui.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.ImageOfTheDay
import com.udacity.asteroidradar.api.Network
import com.udacity.asteroidradar.api.parseAsteroidsJSONResult
import com.udacity.asteroidradar.api.Asteroid
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        fun provideFactory(application: Application) = viewModelFactory {
            initializer {
                MainViewModel(application)
            }
        }
    }

    // For each live data we expose publicly, we use a
    // private mutable variant to encapsulate mutability.
    // This helps avoid bugs.
    private val _asteroidList = MutableLiveData<List<Asteroid>>()

    /**
     * The list of asteroids fetched from the network.
     */
    val asteroidList: LiveData<List<Asteroid>>
        get() = _asteroidList

    private val _imageOfTheDay = MutableLiveData<ImageOfTheDay>()

    /**
     * The image of the day fetched from the network.
     */
    val imageOfTheDay: LiveData<ImageOfTheDay>
        get() = _imageOfTheDay

    private val _errorMessage = MutableSharedFlow<String>()

    /**
     * Network error messages are emitted through this flow.
     */
    val errorMessage: SharedFlow<String>
        get() = _errorMessage

    init {
        // Try to fetch data from the network.
        viewModelScope.launch {
            try {
                fetchAsteroids()
            } catch (e: Exception) {
                // Emit an error message to the fragment
                // if fetching fails.
                _errorMessage.emit(e.message ?: "Unknown error")
            }
        }
    }

    /**
     * Fetches the asteroids from the network,
     * then updates the value of [asteroidList].
     */
    private suspend fun fetchAsteroids() {
        // Format the date today to pass as a query parameter.
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val today = dateFormat.format(Calendar.getInstance().time)
        val asteroidsJSONResult = Network.nasaService.getAsteroidsJSON(startDate = today)
        _asteroidList.value = parseAsteroidsJSONResult(asteroidsJSONResult)
    }
}