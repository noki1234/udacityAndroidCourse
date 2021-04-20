package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg video: AsteroidEntity)

    @Update
    fun update(asteroid: AsteroidEntity)

    @Query("DELETE FROM asteroids_table")
    suspend fun clear()

    @Query("DELETE FROM asteroids_table WHERE closeApproachDate < :startDate")
    fun removeRecordsOlderThanDate(startDate: String)

    @Query("SELECT * FROM asteroids_table WHERE id = :key")
    fun get(key: Long): AsteroidEntity

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate BETWEEN :startDate AND :endDate ORDER BY closeApproachDate ASC")
    fun getAsteroidsByDate(startDate: String, endDate: String = startDate): LiveData<List<AsteroidEntity>>



}