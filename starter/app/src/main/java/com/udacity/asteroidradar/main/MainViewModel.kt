package com.udacity.asteroidradar.main

import AsteroidFilter
import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.NASARepository
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = getDatabase(application)
    private val nasaRepository = NASARepository(database)

    private val _asteroidFilter = MutableLiveData<AsteroidFilter>()

    private val _navigateToSelectedAsteroid = MutableLiveData<Asteroid>()
    val navigateToSelectedAsteroid: LiveData<Asteroid>
        get() = _navigateToSelectedAsteroid

    init {
        viewModelScope.launch {
            _asteroidFilter.value = AsteroidFilter.WEEK
            try {
                nasaRepository.refreshPictureOfTheDay()
                nasaRepository.refreshAsteroids()
            } catch (e: Exception) {
                Timber.e("Error occured when trying to refresh data from NASA: ${e.message}")
            }
        }
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroid.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroid.value = null
    }

    fun updateFilter(filter: AsteroidFilter) {

        _asteroidFilter.value = filter
    }

    val pictureOfTheDay = nasaRepository.pictureOfTheDay
    val asteroids = Transformations.switchMap(_asteroidFilter) { filter ->
        when (filter) {
            AsteroidFilter.TODAY -> nasaRepository.todaysAsteroids
            else -> nasaRepository.asteroids
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}