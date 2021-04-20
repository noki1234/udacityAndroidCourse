package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.api.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class AsteroidApplication: Application() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        coroutineScope.launch { setUpWork() }
    }


    private fun setUpWork(){
        val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .setRequiresBatteryNotLow(true) // Battery cannot be low
                .setRequiresCharging(true) // work will be doing only during charging
                .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(1, TimeUnit.DAYS)
                .setConstraints(constraints).build()

        WorkManager.getInstance()
                .enqueueUniquePeriodicWork(RefreshDataWorker.WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, repeatingRequest)
    }
}