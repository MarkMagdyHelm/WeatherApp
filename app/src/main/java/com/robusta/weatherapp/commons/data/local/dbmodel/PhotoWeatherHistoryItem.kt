package com.robusta.weatherapp.commons.data.local.dbmodel

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "photo_Table")
class PhotoWeatherHistoryItem {
    @PrimaryKey
    @NonNull
    var date: String? = null
    var image: String? = null
}