package com.project.rentakandroid.String.adapter

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.robusta.weatherapp.R
import com.robusta.weatherapp.commons.data.local.dbmodel.PhotoWeatherHistoryItem
import com.robusta.weatherapp.weather.adapter.Interaction
import kotlinx.android.synthetic.main.photo_weather_recycler_item.view.*

class WeatherPhotoHistoryAdapter(var PhotoList: List<PhotoWeatherHistoryItem?>, val interaction: Interaction) : RecyclerView.Adapter<WeatherPhotoHistoryAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherPhotoHistoryAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.photo_weather_recycler_item, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: WeatherPhotoHistoryAdapter.ViewHolder, position: Int) {
        holder.bindItems(PhotoList[position], interaction)
    }

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return PhotoList.size
    }
    fun setphotos(photo: List<PhotoWeatherHistoryItem?>) {
        this.PhotoList= photo
        notifyDataSetChanged()
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item: PhotoWeatherHistoryItem?, interaction: Interaction) {
            itemView.time_date.text = item?.date
            itemView.photo.setImageBitmap(getBitmap(item?.image))
//           itemView.name_tv.text = item?.address

            itemView.setOnClickListener {
                interaction.selectedItem("")
            }
        }

        fun getBitmap(encodedString: String?): Bitmap? {
            return try {
                val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
                BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
            } catch (e: Exception) {
                e.message
                null
            }
        }
    }
}