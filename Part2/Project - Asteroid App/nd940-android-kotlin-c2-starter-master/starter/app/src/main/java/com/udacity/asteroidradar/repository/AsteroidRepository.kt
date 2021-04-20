package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.*
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.lang.Exception

/** Repository for fetching Asteroids from the network and storing them into DB*/
class AsteroidRepository(private val database: AsteroidDatabase){

    /** Asteroids that we show on screen obtained from DB - from TODAY + 7 days*/
    val asteroids: LiveData<List<Asteroid>> = Transformations.map(database.asteroidDao.getAsteroidsByDate(
        getTodayAndLastDayFormattedDate().first, getTodayAndLastDayFormattedDate().second)) {
        it.asDomainModel()
    }



    /** Function for refresh/update offline cache - asteroids*/
    suspend fun refreshAsteroids(startDate: String, endDate: String) {      //call method from coroutine
        //database is input/output operation on disk -> use Dispatcher.IO specific for input/output
        withContext(Dispatchers.IO) {
            try {
                val asteroids = parseAsteroidsJsonResult(JSONObject(AsteroidApi.retrofitService.getJson(startDate, endDate)))
                database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
            } catch (e: Exception) {
                e.printStackTrace()
                println("ERROR: ${e.message}")
            }
        }
    }


}