package com.robusta.weatherapp.commons

import com.egabi.core.application.CoreApp
import com.robusta.weatherapp.weather.di.DaggerWeatherComponent

import com.robusta.weatherapp.weather.di.WeatherComponent
import javax.inject.Singleton


//import javax.inject.Singleton

@Singleton
object CoreDH {

    private var weatherComponent: WeatherComponent? = null
    fun weatherComponent(): WeatherComponent {
        if (weatherComponent == null)
            weatherComponent =
                DaggerWeatherComponent.builder().coreComponent(CoreApp.coreComponent)
                    .build()
        return weatherComponent as WeatherComponent
    }
    fun destroyuserComponent() {
        weatherComponent = null
    }
}