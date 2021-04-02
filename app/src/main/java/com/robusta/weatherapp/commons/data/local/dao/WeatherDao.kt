package com.robusta.weatherapp.commons.data.local.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.robusta.weatherapp.commons.data.local.dbmodel.PhotoWeatherHistoryItem

@Dao
interface WeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(photoHistory: PhotoWeatherHistoryItem?)

    @Query("DELETE FROM photo_Table")
    fun deleteAll()

    @Query("SELECT * from photo_Table ORDER BY Date ASC")
    fun getAllPhotoWeather(): List<PhotoWeatherHistoryItem?>?
}