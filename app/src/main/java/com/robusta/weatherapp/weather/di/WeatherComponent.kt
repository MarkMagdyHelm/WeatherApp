package com.robusta.weatherapp.weather.di

import android.content.Context
import androidx.room.Room
import com.egabi.core.constants.Constants
import com.egabi.core.di.CoreComponent
import com.egabi.core.networking.Scheduler
import com.robusta.weatherapp.commons.data.local.WeatherDb
import com.robusta.weatherapp.commons.data.remote.CoreService
import com.robusta.weatherapp.user.model.WeatherDataContract
import com.robusta.weatherapp.user.model.WeatherRemoteData
import com.robusta.weatherapp.user.model.WeatherRepository
import com.robusta.weatherapp.weather.model.WeatherLocalData
import com.robusta.weatherapp.weather.view.HomeFragment
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModel
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModelFactory
import dagger.Component
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import retrofit2.Retrofit
import javax.inject.Scope


@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class WeatherScope

@WeatherScope
@Component(
    dependencies = [CoreComponent::class],
    modules = [WeatherModule::class]
)
interface WeatherComponent {
    //Expose to dependent components
    fun coreService(): CoreService
    fun inject(Fragment: HomeFragment)
}

@Module
@WeatherScope
class WeatherModule {
    /*ViewModel*/
    @Provides
    @WeatherScope
    fun provideViewModelFactory(
        repository: WeatherDataContract.Repository,
        compositeDisposable: CompositeDisposable
    ): WeatherViewModelFactory =
        WeatherViewModelFactory(repository, compositeDisposable)

    @Provides
    @WeatherScope
    fun provideViewModel(
        repository: WeatherDataContract.Repository,
        compositeDisposable: CompositeDisposable
    ): WeatherViewModel =
        WeatherViewModel(repository, compositeDisposable)

    /*Repository*/
    @Provides
    @WeatherScope
    fun provideRepository(
        remote: WeatherDataContract.Remote,
        scheduler: Scheduler,
        compositeDisposable: CompositeDisposable
    ): WeatherDataContract.Repository =
        WeatherRepository(remote, scheduler, compositeDisposable)

    @Provides
    @WeatherScope
    fun provideremoteData(coreService: CoreService): WeatherDataContract.Remote =
        WeatherRemoteData(coreService)


    @Provides
    @WeatherScope
    fun providecompositeDisposable(): CompositeDisposable = CompositeDisposable()

    @Provides
    @WeatherScope
    fun providelocalData(postDb: WeatherDb, scheduler: Scheduler): WeatherDataContract.Local = WeatherLocalData(postDb, scheduler)
    /*Parent providers to dependents*/
    @Provides
    @WeatherScope
    fun provideWeatherDb(context: Context): WeatherDb = Room.databaseBuilder(context, WeatherDb::class.java, Constants.DB_NAME).allowMainThreadQueries().build()


    @Provides
    @WeatherScope
    fun provideService(retrofit: Retrofit): CoreService = retrofit.create(CoreService::class.java)
}