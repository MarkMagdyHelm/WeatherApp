package com.robusta.weatherapp.user.model

import com.egabi.core.extensions.*
import com.egabi.core.networking.Outcome
import com.egabi.core.networking.Scheduler
import com.robusta.weatherapp.commons.data.remote.WeatherResponse
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject


class WeatherRepository(
    private val remote: WeatherDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable

) : WeatherDataContract.Repository {
    override var getWeatherDetailsOutcome: PublishSubject<Outcome<WeatherResponse>> = PublishSubject.create()

    override fun getWeatherDetails(longitude: Double, latitude: Double) {
        getWeatherDetailsOutcome.loading(true)
        return remote.getWeatherDetails(longitude,latitude).performOnBackOutOnMain(scheduler)
            .subscribe({
                getWeatherDetailsOutcome.success(it)
            }, { error -> getWeatherDetailsOutcome.failed(error) })
            .addTo(compositeDisposable)
    }

}