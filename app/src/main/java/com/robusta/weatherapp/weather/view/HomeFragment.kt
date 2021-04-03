package com.robusta.weatherapp.weather.view

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.robusta.weatherapp.R
import com.robusta.weatherapp.commons.BaseFragment
import com.robusta.weatherapp.commons.CoreDH
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModel
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*

import javax.inject.Inject


@Suppress("DEPRECATION")
class HomeFragment : BaseFragment() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val component by lazy { CoreDH.weatherComponent() }
    val REQUEST_IMAGE_CAPTURE = 1
    @Inject
    lateinit var viewModelFactory: WeatherViewModelFactory

    @Inject
    lateinit var viewModel: WeatherViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        component.inject(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        get_weather_details_bt.setOnClickListener {
            dispatchTakePictureIntent()
         }

    }
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val extras = data?.extras
            val imageBitmap = extras?.get("data") as Bitmap
            viewModel.setBackgroundImage(imageBitmap)
            navigateToPhotoWeatherScreen()
        }
    }

    private fun navigateToPhotoWeatherScreen() {
        val navController = activity?.findNavController(R.id.main_host_fragment)
        navController?.navigate(R.id.photoWeatherFragment, null)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}