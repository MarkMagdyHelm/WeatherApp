package com.robusta.weatherapp.weather.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.egabi.core.constants.Constants
import com.robusta.weatherapp.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main_activity_base)
        requestLocationPermission()
    }

    private fun requestLocationPermission() {
        val permReqLuncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {

            Constants.canAccessLocationPermission = it
        }

        when (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            PackageManager.PERMISSION_GRANTED -> {

            }
            PackageManager.PERMISSION_DENIED -> {

                permReqLuncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
            }
        }
    }

}