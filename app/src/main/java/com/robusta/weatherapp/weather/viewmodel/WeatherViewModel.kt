package com.robusta.weatherapp.weather.viewmodel

import androidx.lifecycle.LiveData
import com.egabi.core.extensions.toLiveData
import com.egabi.core.networking.Outcome
import com.jakewharton.rxrelay2.BehaviorRelay
import com.robusta.weatherapp.commons.BaseViewModel
import com.robusta.weatherapp.commons.CoreDH
import com.robusta.weatherapp.user.model.WeatherDataContract
import com.robusta.weatherapp.commons.data.local.UserResponseModel
import com.robusta.weatherapp.commons.data.remote.WeatherResponse
import io.reactivex.disposables.CompositeDisposable

class WeatherViewModel(
    private val repo: WeatherDataContract.Repository,
    private val compositeDisposable: CompositeDisposable
) : BaseViewModel() {
    lateinit var getWeatherDetailsOutcome: LiveData<Outcome<WeatherResponse>>

    fun getWeatherDetails(longitude :Double,latitude :Double) {
        repo.getWeatherDetails(longitude ,latitude)
    }

    fun initGetWeatherDetailsOutcome() {
        getWeatherDetailsOutcome =
            repo.getWeatherDetailsOutcome.toLiveData(compositeDisposable)
    }
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        CoreDH.destroyuserComponent()
    }
}