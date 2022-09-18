package com.udacity.asteroidradar.api

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val NEOWS_URI =
    "neo/rest/v1/feed?api_key=${API_KEY}"

private val retrofit =
    Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(Constants.BASE_URL).build()

interface NeoWsAPIService {
    @GET(NEOWS_URI)
    fun getAsteroids(
    ): Call<String>
}

object NeoWsApi {
    val retrofitService: NeoWsAPIService by lazy {
        retrofit.create(NeoWsAPIService::class.java)
    }
}