package com.robusta.weatherapp.user.model

import com.egabi.core.networking.Outcome
import com.robusta.weatherapp.commons.data.local.LoginPostModel
import com.robusta.weatherapp.commons.data.local.UserResponseModel
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
}