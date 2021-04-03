package com.robusta.weatherapp.weather.model

import com.egabi.core.networking.Scheduler
import com.egabi.core.extensions.performOnBack
import com.jakewharton.rxrelay2.BehaviorRelay
import com.robusta.weatherapp.commons.data.local.WeatherDb
import com.robusta.weatherapp.commons.data.local.dbmodel.PhotoWeatherHistoryItem
import com.robusta.weatherapp.commons.data.remote.WeatherResponse
import com.robusta.weatherapp.user.model.WeatherDataContract
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers


class WeatherLocalData(private val weatherDb: WeatherDb, private val scheduler: Scheduler) : WeatherDataContract.Local {



    override fun insertPhoto(photo: PhotoWeatherHistoryItem) {
        weatherDb.weatherDao().insert(photo)
    }

    override fun deletePhoto() {
        weatherDb.weatherDao().deleteAll()
    }

    override fun getPhotos(): Single<List<PhotoWeatherHistoryItem?>?> {
        return initialize(weatherDb.weatherDao().getAllPhotoWeather() )
    }
    private fun initialize(list:List<PhotoWeatherHistoryItem?>?): Single<List<PhotoWeatherHistoryItem?>?> {
        return Single.create<List<PhotoWeatherHistoryItem?>?> { singleEmitter ->
            singleEmitter.onSuccess(list!!) }
                .observeOn(Schedulers.io()) }
}

