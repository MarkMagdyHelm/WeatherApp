package com.robusta.weatherapp.weather.viewmodel

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Environment
import android.util.Base64
import androidx.lifecycle.LiveData
import com.egabi.core.constants.Constants
import com.egabi.core.extensions.toLiveData
import com.egabi.core.networking.Outcome
import com.jakewharton.rxrelay2.BehaviorRelay
import com.robusta.weatherapp.commons.BaseViewModel
import com.robusta.weatherapp.commons.CoreDH
import com.robusta.weatherapp.commons.data.local.dbmodel.PhotoWeatherHistoryItem
import com.robusta.weatherapp.commons.data.remote.WeatherResponse
import com.robusta.weatherapp.user.model.WeatherDataContract
import io.reactivex.disposables.CompositeDisposable
import java.io.*
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(
        private val repo: WeatherDataContract.Repository,
        private val compositeDisposable: CompositeDisposable
) : BaseViewModel() {
    lateinit var getWeatherDetailsOutcome: LiveData<Outcome<WeatherResponse>>
    lateinit var getWeatherPhotoHistoryOutcome: LiveData<Outcome<List<PhotoWeatherHistoryItem?>?>>

    var placeTextObservable = BehaviorRelay.createDefault<String>("")
    var backgroundImage = BehaviorRelay.create<Bitmap>()
    var locationObservable = BehaviorRelay.create<Location>()

    fun getWeatherDetails(longitude: Double, latitude: Double) {
        repo.getWeatherDetails(longitude, latitude)
    }

    fun initGetWeatherDetailsOutcome() {
        getWeatherDetailsOutcome =
                repo.getWeatherDetailsOutcome.toLiveData(compositeDisposable)
    }
    fun initWeatherPhotoHistoryOutcome() {
        getWeatherPhotoHistoryOutcome =
                repo.getWeatherPhotoHistoryOutcome.toLiveData(compositeDisposable)
    }
    fun setBackgroundImage(imageBitmap: Bitmap) {
        backgroundImage.accept(imageBitmap)
    }
    fun getLocation(location: Location) {
        locationObservable.accept(location)
    }
    fun getAddress(lat: Double, lng: Double, geocoder: Geocoder) {
        var addresses: List<Address>? = null
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1)
            val obj = addresses[0]
            placeTextObservable.accept(obj.getAddressLine(0))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    @SuppressLint("SimpleDateFormat")
    fun storePhoto(takescreenshotOfRootView: Bitmap) {
        val dateFormat = SimpleDateFormat(Constants.DateFormat)
        val photoWeatherItemObj = PhotoWeatherHistoryItem()
        photoWeatherItemObj.date = dateFormat.format(Calendar.getInstance().time)
        photoWeatherItemObj.image = convertBitmapToBase24(takescreenshotOfRootView)
        repo.insertPhoto(photoWeatherItemObj)
    }

    fun convertBitmapToBase24(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun storeScreenshot(bitmap: Bitmap, filename: String) {
        val path = Environment.getExternalStorageDirectory().toString() + "/" + filename
        var out: OutputStream? = null
        val imageFile = File(path)
        try {
            out = FileOutputStream(imageFile)
            // choose JPEG format
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
        } catch (e: FileNotFoundException) {
            // manage exception ...
        } catch (e: IOException) {
            // manage exception ...
        } finally {
            try {
                out?.close()
            } catch (exc: Exception) {
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
        CoreDH.destroyuserComponent()
    }

    fun getWeatherPhotosHistory() {
        repo.getPhotos()
    }


}