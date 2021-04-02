package com.robusta.weatherapp.commons.data.remote

import com.robusta.weatherapp.commons.data.local.UserResponseModel
import io.reactivex.Single
import retrofit2.http.*

interface CoreService {

    @POST
    fun login(
        @Body obj: LoginPostModel,
        @Url url: String
    ): Single<UserResponseModel>

}

