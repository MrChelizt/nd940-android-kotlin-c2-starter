package com.udacity.asteroidradar.repository

import android.provider.ContactsContract.Data
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfTheDay
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.DatabaseAsteroid
import com.udacity.asteroidradar.database.NASADatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

class NASARepository(private val database: NASADatabase) {

    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    val todaysAsteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getTodaysAsteroids(getToday())) {
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroidCall = NeoWsApi.retrofitService.getAsteroids()
            val asteroidsResponse = asteroidCall.execute()
            var asteroidz = asteroidsResponse.body()?.let {
                parseAsteroidsJsonResult(JSONObject(it))
            }
            database.asteroidDao.deleteOldAsteroids(getToday())
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

    private fun getToday(): String {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }
}

