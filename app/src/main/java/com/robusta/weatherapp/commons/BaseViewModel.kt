package com.robusta.weatherapp.commons

import androidx.lifecycle.ViewModel
import com.jakewharton.rxrelay2.BehaviorRelay

open class BaseViewModel : ViewModel() {
    val error = BehaviorRelay.create<String>()
    val success = BehaviorRelay.create<String>()
}