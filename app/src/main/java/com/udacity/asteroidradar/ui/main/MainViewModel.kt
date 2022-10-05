package com.udacity.asteroidradar.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

class MainViewModel(application: Application) : AndroidViewModel(application) {
    companion object {
        fun provideFactory(application: Application) = viewModelFactory {
            initializer {
                MainViewModel(application)
            }
        }
    }
}