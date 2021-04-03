package com.egabi.core.constants


object Constants {
    var canAccessLocationPermission = false
    val API_URL =
        "https://api.openweathermap.org/data/2.5/"
    const val DB_NAME = "weather_db"
    var appid = "9424875d7bb93d22f0056e7073d62c77"
    var language = "EN"
    var DateFormat = "yyyy-mm-dd hh:mm:ss"
    var deviceType = "android"
    var devicePushToken = ""
    var token = ""
    var isGuest = false
    val langCode: String
        inline get() {
            return if (language == "EN") {
                "En"
            } else {
                "Ar"
            }
        }



}

