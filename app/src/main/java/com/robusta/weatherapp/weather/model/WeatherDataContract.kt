package com.robusta.weatherapp.user.model

import com.egabi.core.networking.Outcome
import com.robusta.weatherapp.commons.data.remote.LoginPostModel
import com.robusta.weatherapp.commons.data.local.UserResponseModel
import com.robusta.weatherapp.commons.data.local.dbmodel.PhotoWeatherHistoryItem
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

class WeatherDataContract {
    interface Repository {
        val getUserDataFetchOutcome: PublishSubject<Outcome<UserResponseModel>>
        fun login(obj: LoginPostModel)
    }

    interface Remote {
        fun login(obj: LoginPostModel): Single<UserResponseModel>
    }

    interface Local {
        fun insertPhoto(photo: PhotoWeatherHistoryItem)
        fun deletePhoto()
        fun getPhotos(): List<PhotoWeatherHistoryItem?>?
    }
}