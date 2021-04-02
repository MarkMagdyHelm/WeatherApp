package com.robusta.weatherapp.user.model


import com.egabi.core.constants.Constants
import com.robusta.weatherapp.commons.data.remote.CoreService
import com.robusta.weatherapp.commons.data.remote.WeatherResponse

import io.reactivex.Single
class WeatherRemoteData(private val service: CoreService) :
    WeatherDataContract.Remote {
    override fun getWeatherDetails(longitude :Double,latitude :Double) : Single<WeatherResponse>{
        return service.getWeatherDetails(longitude ,latitude, Constants.appid)
    }

}