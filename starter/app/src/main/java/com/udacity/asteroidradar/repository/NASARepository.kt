package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.NASADatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class NASARepository(private val database: NASADatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidCall = NeoWsApi.retrofitService.getAsteroids()
            val asteroidsResponse = asteroidCall.execute()
            var asteroidz = asteroidsResponse.body()?.let {
                parseAsteroidsJsonResult(JSONObject(it))
            }
            if (asteroidz != null && asteroidz.size > 0)
                database.asteroidDao.insertAll(*asteroidz?.asDatabaseModel())
        }
    }

    val pictureOfTheDay: LiveData<PictureOfTheDay> =
        Transformations.map(database.pictureOfTheDayDao.getPictureOfTheDay()) {
            it?.asDomainModel()
        }

    suspend fun refreshPictureOfTheDay() {
        withContext(Dispatchers.IO) {
            val picture = APODApi.retrofitService.getAPOD()
            database.pictureOfTheDayDao.insert(picture.asDatabaseModel())
        }
    }
}