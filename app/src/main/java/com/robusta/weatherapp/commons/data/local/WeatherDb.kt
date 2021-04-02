package com.robusta.weatherapp.commons.data.local


import androidx.room.Database
import androidx.room.RoomDatabase
import com.robusta.weatherapp.commons.data.local.dao.WeatherDao
import com.robusta.weatherapp.commons.data.local.dbmodel.PhotoWeatherHistoryItem

@Database(entities = [PhotoWeatherHistoryItem::class], version = 3,exportSchema = false)
abstract class WeatherDb : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
