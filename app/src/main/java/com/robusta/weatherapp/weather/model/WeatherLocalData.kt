package com.robusta.weatherapp.weather.model

import com.egabi.core.networking.Scheduler
import com.egabi.core.extensions.performOnBack
import com.robusta.weatherapp.commons.data.local.WeatherDb
import com.robusta.weatherapp.commons.data.local.dbmodel.PhotoWeatherHistoryItem
import com.robusta.weatherapp.user.model.WeatherDataContract



class WeatherLocalData(private val weatherDb: WeatherDb, private val scheduler: Scheduler) : WeatherDataContract.Local {



    override fun insertPhoto(photo: PhotoWeatherHistoryItem) {
        weatherDb.weatherDao().insert(photo)
    }

    override fun deletePhoto() {
        weatherDb.weatherDao().deleteAll()
    }

    override fun getPhotos(): List<PhotoWeatherHistoryItem?>? {
        return weatherDb.weatherDao().getAllPhotoWeather()
    }

}

