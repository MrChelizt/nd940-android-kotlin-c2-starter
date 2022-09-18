package com.udacity.asteroidradar.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R

class DetailViewModel(asteroid: Asteroid, app: Application) :
    AndroidViewModel(app) {
    private val _selectedAsteroid = MutableLiveData<Asteroid>()
    val selectedAsteroid: LiveData<Asteroid>
        get() = _selectedAsteroid

    init {
        _selectedAsteroid.value = asteroid
    }

    val displayHazardousType = Transformations.map(selectedAsteroid) {
        app.applicationContext.getDrawable(
            when (it.isPotentiallyHazardous){
                true -> R.drawable.asteroid_hazardous
                false -> R.drawable.asteroid_safe
            }
        )
    }

}