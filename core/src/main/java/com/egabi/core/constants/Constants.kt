package com.egabi.core.constants


object Constants {
    val API_URL =
        "https://api.moussaacademy.com/api/"

    var language = "EN"

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
    object Posts {
        const val DB_NAME = "weather_db"
    }


}

