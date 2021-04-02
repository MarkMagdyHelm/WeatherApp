package com.robusta.weatherapp.commons.data.local

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


class UserResponseModel {
    @SerializedName("Succeeded")
    @Expose
    var succeeded: Boolean? = null

    @SerializedName("Message")
    @Expose
    var message: String? = null

    @SerializedName("Errors")
    @Expose
    var errors: List<Error>? = null

    @SerializedName("Data")
    @Expose
    var data: UserData? = null


}

class Error {
    @SerializedName("ErrorCode")
    @Expose
    var errorCode: Int? = null

    @SerializedName("ErrorMessage")
    @Expose
    var errorMessage: String? = null
}

class UserData {
    @SerializedName("BearerToken")
    @Expose
    var bearerToken: String? = null

    @SerializedName("Fullname")
    @Expose
    var fullname: String? = null

    @SerializedName("Email")
    @Expose
    var email: String? = null

    @SerializedName("PhoneNumber")
    @Expose
    var phoneNumber: String? = null
}
