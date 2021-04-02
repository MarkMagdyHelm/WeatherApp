package com.robusta.weatherapp.weather.viewmodel

import androidx.lifecycle.LiveData
import com.egabi.core.constants.Constants
import com.egabi.core.extensions.toLiveData
import com.egabi.core.networking.Outcome
import com.jakewharton.rxrelay2.BehaviorRelay
import com.robusta.weatherapp.commons.BaseViewModel
import com.robusta.weatherapp.commons.CoreDH
import com.robusta.weatherapp.user.model.WeatherDataContract
import com.robusta.weatherapp.commons.data.local.LoginPostModel
import com.robusta.weatherapp.commons.data.local.UserResponseModel
import io.reactivex.disposables.CompositeDisposable

class WeatherViewModel(
    private val repo: WeatherDataContract.Repository,
    private val compositeDisposable: CompositeDisposable
) : BaseViewModel() {
    var getUserDataFetchOutcome: LiveData<Outcome<UserResponseModel>> =
        repo.getUserDataFetchOutcome.toLiveData(compositeDisposable)
    var phonenumber = BehaviorRelay.create<String>()

    fun login() {
        val obj = LoginPostModel()
        obj.userName = phonenumber.value
        obj.password = "@Stud0nt"
        obj.devicePushToken = Constants.devicePushToken
        repo.login(obj)
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        CoreDH.destroyuserComponent()
    }
}