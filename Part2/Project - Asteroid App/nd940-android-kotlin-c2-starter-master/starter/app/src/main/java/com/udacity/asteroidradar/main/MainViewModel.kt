package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.getTodayAndLastDayFormattedDate
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _podUrl = MutableLiveData<String>()
    val podUrl: LiveData<String>
    get() = _podUrl

    private val _podTitle = MutableLiveData<String>()
    val podTitle: LiveData<String>
    get() = _podTitle


    //Add a _navigateToSelectedProperty MutableLiveData externalized as LiveData
    private val _navigateToSelectedProperty = MutableLiveData<Asteroid>()
    val navigateToSelectedProperty : LiveData<Asteroid>
        get() = _navigateToSelectedProperty



    private val database = getDatabase(application)
    private val repository = AsteroidRepository(database)

    init{
        viewModelScope.launch {
            repository.refreshAsteroids(getTodayAndLastDayFormattedDate().first, getTodayAndLastDayFormattedDate().second)
            getPictureOfTheDayUrl()
        }
    }

    val asteroids = repository.asteroids

    fun navigateTo(asteroid: Asteroid){
        _navigateToSelectedProperty.value = asteroid
    }

    fun navigateToCompleted(){
        _navigateToSelectedProperty.value = null
    }

    private suspend fun getPictureOfTheDayUrl(){
            try {
                val pod = AsteroidApi.retrofitService.getPictureOfTheDay()
                _podUrl.value = pod.url
                _podTitle.value =  String.format(getString(R.string.nasa_picture_of_day_content_description_format), pod.title)
            }catch (e: Exception){
                _podUrl.value = getString(R.string.error)
                _podTitle.value = String.format(getString(R.string.nasa_picture_of_day_content_description_format), getString(R.string.error_NotFound))
                e.printStackTrace()
            }
    }

    private fun getString(res: Int) : String {
        return getApplication<Application>().resources.getString(res)
    }



}