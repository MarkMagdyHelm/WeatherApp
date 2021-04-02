package com.robusta.weatherapp.weather.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.robusta.weatherapp.user.model.WeatherDataContract
import io.reactivex.disposables.CompositeDisposable


class WeatherViewModelFactory(
    private val repository: WeatherDataContract.Repository,
    private val compositeDisposable: CompositeDisposable
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherViewModel(repository, compositeDisposable) as T
    }
}