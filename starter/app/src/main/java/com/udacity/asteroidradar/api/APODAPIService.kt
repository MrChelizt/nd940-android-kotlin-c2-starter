package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val APOD_URI = "planetary/apod?api_key=${API_KEY}"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder().addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface APODAPIService {
    @GET(APOD_URI)
    suspend fun getAPOD(): NetworkPictureOfTheDay
}

object APODApi {
    val retrofitService: APODAPIService by lazy {
        retrofit.create(APODAPIService::class.java)
    }
}