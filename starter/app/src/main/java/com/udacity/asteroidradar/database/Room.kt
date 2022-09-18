package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM DatabaseAsteroid")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM DatabaseAsteroid WHERE closeApproachDate = :today")
    fun getTodaysAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroid: DatabaseAsteroid)

    @Query("DELETE FROM DatabaseAsteroid WHERE date(closeApproachDate) < date(:today)")
    fun deleteOldAsteroids(today: String)
}

@Dao
interface PictureOfTheDayDao {
    @Query("SELECT * FROM DatabasePictureOfTheDay")
    fun getPictureOfTheDay(): LiveData<DatabasePictureOfTheDay>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(pictureOfTheDay: DatabasePictureOfTheDay)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePictureOfTheDay::class], version = 1)
abstract class NASADatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val pictureOfTheDayDao: PictureOfTheDayDao
}

private lateinit var INSTANCE: NASADatabase
fun getDatabase(context: Context): NASADatabase {
    synchronized(NASADatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                NASADatabase::class.java, "nasa"
            ).build()
        }
        return INSTANCE
    }
}


