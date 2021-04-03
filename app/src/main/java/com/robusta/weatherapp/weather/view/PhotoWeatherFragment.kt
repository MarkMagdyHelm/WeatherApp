package com.robusta.weatherapp.weather.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.observe
import androidx.navigation.findNavController
import com.egabi.core.extensions.addTo
import com.egabi.core.networking.Outcome
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.robusta.weatherapp.BuildConfig
import com.robusta.weatherapp.R
import com.robusta.weatherapp.commons.BaseFragment
import com.robusta.weatherapp.commons.CoreDH
import com.robusta.weatherapp.commons.Screenshot
import com.robusta.weatherapp.commons.data.remote.WeatherResponse
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModel
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_photo_weather.*
import kotlinx.android.synthetic.main.fragment_photo_weather.view.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import javax.inject.Inject


@Suppress("DEPRECATION")
class PhotoWeatherFragment : BaseFragment(),View.OnClickListener {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val component by lazy { CoreDH.weatherComponent() }

    @Inject
    lateinit var viewModelFactory: WeatherViewModelFactory

    @Inject
    lateinit var viewModel: WeatherViewModel
    private var fusedLocationClient: FusedLocationProviderClient? = null

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_photo_weather, container, false)
        component.inject(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getLocation()
        initButtonsListeners()
        observeViewChanges()
        bindGetWeatherDetails()
    }


    private fun observeViewChanges() {
        viewModel.locationObservable.subscribe {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            viewModel.getAddress(it.latitude, it.longitude, geocoder)
            this.viewModel.getWeatherDetails(it.latitude, it.longitude)
        }.addTo(compositeDisposable)

        viewModel.backgroundImage.subscribe {
            view?.picked_imageview?.setImageBitmap(it)
        }.addTo(compositeDisposable)
        viewModel.placeTextObservable.subscribe {
            view?.place_name?.text = it
        }.addTo(compositeDisposable)
    }

    private fun initButtonsListeners() {
        save_btn.setOnClickListener(this)
        share_btn.setOnClickListener(this)
    }



    private fun getLocation() {
        if (checkLocationPermission()) return
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient!!.lastLocation.addOnSuccessListener(requireActivity()
        ) { location -> // Got last known location. In some rare situations this can be null.
            if (location != null) {
                viewModel.getLocation(location)
            } else Toast.makeText(
                    requireContext(),
                    getString(R.string.cant_get_location),
                    Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun checkLocationPermission(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }



    private fun bindGetWeatherDetails() {
        viewModel.initGetWeatherDetailsOutcome()
        viewModel.getWeatherDetailsOutcome.observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Progress ->
                    if (outcome.loading) {
                        showdialog()
                    } else {
                        dismissdialog()
                    }
                is Outcome.Success -> {
                    dismissdialog()
                    view?.let { bindWeatherData(outcome.data, it) }
                }
                is Outcome.Failure -> {
                    dismissdialog()
                }

            }
        }

    }
    override fun onClick(view: View) {
        when (view.id) {
            R.id.save_btn -> {
                viewModel.storePhoto(Screenshot.takescreenshotOfRootView(main_layout))
                navigateToHistoryScreen()
            }
            R.id.share_btn -> {
                viewModel.storeScreenshot(Screenshot.takescreenshotOfRootView(main_layout), "weatherScreen")
//                getBitmapFromView(Screenshot.takescreenshotOfRootView(main_layout))
                shareImage()

            }
        }
    }
    private fun navigateToHistoryScreen() {
        val navController = activity?.findNavController(R.id.main_host_fragment)
        navController?.navigate(R.id.weatherPhotosHistoryFragment, null)
    }

    private fun bindWeatherData(weatherResponseObj: WeatherResponse,view: View) {
        view.temp_tv.text = weatherResponseObj.main?.temp.toString()
        view.max_value.text = weatherResponseObj.main?.tempMax.toString()
        view.min_value.text = weatherResponseObj.main?.tempMin.toString()
        view.weather_disc_tv.text = weatherResponseObj.weather?.get(0)?.description
        view.pressure_value_tv.text = weatherResponseObj.main?.pressure.toString()
        view.humidity_value_tv.text = weatherResponseObj.main?.humidity.toString()
        view.winddegree_value_tv.text = weatherResponseObj.wind?.deg.toString()
        view.windspeed_value_tv.text = weatherResponseObj.wind?.speed.toString()
    }
    private fun shareImage() {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(Intent.EXTRA_STREAM, getBitmapFromView(Screenshot.takescreenshotOfRootView(main_layout)))
        startActivity(Intent.createChooser(intent, "Share Image"))
    }
    private fun getBitmapFromView(bmp: Bitmap?): Uri? {
        var bmpUri: Uri? = null
        try {
            val file = File(requireContext().filesDir.absolutePath, System.currentTimeMillis().toString() + ".jpg")
            val out = FileOutputStream(file)
            bmp?.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(requireActivity(), BuildConfig.APPLICATION_ID + ".provider",file)

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return bmpUri

    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}