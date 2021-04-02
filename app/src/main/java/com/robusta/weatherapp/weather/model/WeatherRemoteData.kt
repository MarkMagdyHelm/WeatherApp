package com.robusta.weatherapp.user.model


import com.egabi.core.constants.Constants
import com.robusta.weatherapp.commons.data.local.LoginPostModel
import com.robusta.weatherapp.commons.data.local.UserResponseModel
import com.robusta.weatherapp.commons.data.remote.CoreService
import io.reactivex.Single
class WeatherRemoteData(private val service: CoreService) :
    WeatherDataContract.Remote {
    override fun login(obj: LoginPostModel) : Single<UserResponseModel>{
        return service.login(obj,Constants.API_URL+"Account/Login")
    }

}