package com.udacity.asteroidradar.work

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.NASARepository
import retrofit2.HttpException
import timber.log.Timber

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = NASARepository(database)
        return try {
            repository.refreshAsteroids()
            repository.refreshPictureOfTheDay()
            Result.success()
        } catch (e: HttpException) {
            Timber.e("Http exception occured")
            Result.retry()
        }
    }
}