package com.udacity.asteroidradar.api

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDatabase.Companion.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class RefreshDataWorker(context: Context, params: WorkerParameters): CoroutineWorker(context, params) {

    companion object{
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            database.asteroidDao.removeRecordsOlderThanDate(getTodayAndLastDayFormattedDate().first)
            repository.refreshAsteroids(getTodayAndLastDayFormattedDate().first, getTodayAndLastDayFormattedDate().second)
            Result.success()
        }catch (e: HttpException){
            Result.retry() // Retry to reconnect
        }
    }
}