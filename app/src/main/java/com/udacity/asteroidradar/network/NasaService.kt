package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * An object that provides methods for
 * fetching data from the network.
 */
interface NasaService {
    @GET(Constants.ASTEROIDS_URL)
    suspend fun getAsteroidsJSON(
        @Query(Constants.START_DATE_QUERY) startDate: String,
        @Query(Constants.API_KEY_QUERY) apiKey: String = Constants.API_KEY
    ): String

    @GET(Constants.IMAGE_OF_THE_DAY_URL)
    suspend fun getImageOfTheDay(
        @Query(Constants.API_KEY_QUERY) apiKey: String = Constants.API_KEY
    ) : ImageOfTheDay
}

/**
 * A singleton holding an instance of the Nasa service.
 */
object Network {
    /**
     * A lazy initialized Nasa service.
     */
    val nasaService: NasaService by lazy {
        // Moshi is responsible for automatically parsing
        // the image of the day's JSON response.
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        // Retrofit creates the service with the moshi converter
        // and scalars converter, which is needed for converting
        // the JSON response to a string.
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        retrofit.create(NasaService::class.java)
    }
}