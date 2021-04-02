package com.robusta.weatherapp.commons.data.remote

import io.reactivex.Single
import retrofit2.http.*

interface CoreService {
    @GET("weather")
    fun getWeatherDetails(
        @Query("lat") lat: Double?,
        @Query("lon") lon: Double?,
        @Query("appid") appid: String?
    ): Single<WeatherResponse>
}

