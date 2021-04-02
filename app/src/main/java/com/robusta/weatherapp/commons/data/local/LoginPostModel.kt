package com.robusta.weatherapp.commons.data.local

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LoginPostModel {
    @SerializedName("UserName")
    @Expose
    var userName: String? = null

    @SerializedName("DevicePushToken")
    @Expose
    var devicePushToken: String? = null

    @SerializedName("DeviceId")
    @Expose
    var deviceId: String? = null

    @SerializedName("Password")
    @Expose
    var password: String? = null
}