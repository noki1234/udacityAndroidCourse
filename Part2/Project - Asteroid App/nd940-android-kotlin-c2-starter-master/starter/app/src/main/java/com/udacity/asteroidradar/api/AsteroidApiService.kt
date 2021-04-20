package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.Constants.BASE_URL
import com.udacity.asteroidradar.PictureOfDay
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Query


interface AsteroidApiService {

    @GET("planetary/apod?api_key=$API_KEY")
    suspend fun getPictureOfTheDay(): PictureOfDay

    @GET("neo/rest/v1/feed?&api_key=$API_KEY")
    suspend fun getJson(@Query("start_date") startDate: String, @Query("end_date") endDate: String): String
}

object AsteroidApi{
    val retrofitService: AsteroidApiService by lazy {
        retrofit.create(AsteroidApiService::class.java)
    }
}

private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build() //Moshi builder to create a moshi object with the KotlinJsonAdapterFactory
private val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(MoshiConverterFactory.create(
    moshi)).baseUrl(BASE_URL).build()