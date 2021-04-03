package com.robusta.weatherapp.user.model

import com.egabi.core.networking.Outcome
import com.robusta.weatherapp.commons.data.remote.LoginPostModel
import com.robusta.weatherapp.commons.data.local.UserResponseModel
import com.robusta.weatherapp.commons.data.local.dbmodel.PhotoWeatherHistoryItem
import com.robusta.weatherapp.commons.data.remote.WeatherResponse
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class WeatherDataContract {
    interface Repository {
        var getWeatherDetailsOutcome: PublishSubject<Outcome<WeatherResponse>>
        var getWeatherPhotoHistoryOutcome: PublishSubject<Outcome<List<PhotoWeatherHistoryItem?>?>>

        fun  getWeatherDetails(longitude :Double,latitude :Double)
        fun insertPhoto(photo: PhotoWeatherHistoryItem)
        fun deletePhoto()
        fun getPhotos()
    }

    interface Remote {
      fun  getWeatherDetails(longitude :Double,latitude :Double) : Single<WeatherResponse>
    }

    interface Local {
        fun insertPhoto(photo: PhotoWeatherHistoryItem)
        fun deletePhoto()
        fun getPhotos():Single<List<PhotoWeatherHistoryItem?>?>
    }
}