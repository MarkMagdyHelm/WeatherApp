package com.robusta.weatherapp.user.model

import com.egabi.core.extensions.*
import com.egabi.core.networking.Outcome
import com.egabi.core.networking.Scheduler
import com.robusta.weatherapp.commons.data.local.LoginPostModel
import com.robusta.weatherapp.commons.data.local.UserResponseModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject


class WeatherRepository(
    private val remote: WeatherDataContract.Remote,
    private val scheduler: Scheduler,
    private val compositeDisposable: CompositeDisposable

) : WeatherDataContract.Repository {
    override val getUserDataFetchOutcome: PublishSubject<Outcome<UserResponseModel>>
       = PublishSubject.create()


    override fun login(obj: LoginPostModel) {
        getUserDataFetchOutcome.loading(true)
        return remote.login(obj).performOnBackOutOnMain(scheduler)
            .subscribe({
                    getUserDataFetchOutcome.success(it)
            }, { error -> getUserDataFetchOutcome.failed(error) })
            .addTo(compositeDisposable)
    }

}