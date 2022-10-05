package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.Constants
import retrofit2.Retrofit
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
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String = Constants.API_KEY
    ): String
}

/**
 * A singleton holding an instance
 * of the Nasa service.
 */
object Network {
    /**
     * A lazy initialized Nasa service.
     */
    val nasaService: NasaService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
        retrofit.create(NasaService::class.java)
    }
}