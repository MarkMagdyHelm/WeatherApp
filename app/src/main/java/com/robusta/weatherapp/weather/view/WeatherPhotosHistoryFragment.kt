package com.robusta.weatherapp.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.content.Intent
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.egabi.core.networking.Outcome
import com.project.rentakandroid.String.adapter.WeatherPhotoHistoryAdapter
import com.robusta.weatherapp.R
import com.robusta.weatherapp.commons.BaseFragment
import com.robusta.weatherapp.commons.CoreDH
import com.robusta.weatherapp.commons.data.local.dbmodel.PhotoWeatherHistoryItem
import com.robusta.weatherapp.weather.adapter.Interaction
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModel
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_photo_history.*
import javax.inject.Inject


@Suppress("DEPRECATION")
class WeatherPhotosHistoryFragment : BaseFragment() ,Interaction {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val component by lazy { CoreDH.weatherComponent() }

    @Inject
    lateinit var viewModelFactory: WeatherViewModelFactory

    @Inject
    lateinit var viewModel: WeatherViewModel
    private var weatherPhotoHistoryAdapter: WeatherPhotoHistoryAdapter? = null
    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.fragment_photo_history, container, false)
        component.inject(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindGetWeatherHistory()
        handleBackPress()

    }


    fun bindGetWeatherHistory() {
        viewModel.initWeatherPhotoHistoryOutcome()
        viewModel.getWeatherPhotoHistoryOutcome.observe(viewLifecycleOwner) { outcome ->
            when (outcome) {
                is Outcome.Progress ->
                    if (outcome.loading) {
                        showdialog()
                    } else {
                        dismissdialog()
                    }
                is Outcome.Success -> {
                    dismissdialog()
                    initAddressesRecycler(outcome.data!!)

                }
                is Outcome.Failure -> {
                    dismissdialog()
                }

            }
        }
        viewModel.getWeatherPhotosHistory()
    }
    private fun initAddressesRecycler(packageList: List<PhotoWeatherHistoryItem?>) {
        photo_history_recycler.layoutManager = LinearLayoutManager(activity)
        weatherPhotoHistoryAdapter = WeatherPhotoHistoryAdapter(packageList, this)
        photo_history_recycler.adapter = weatherPhotoHistoryAdapter
    }
    private fun handleBackPress() {
        activity?.onBackPressedDispatcher?.addCallback(requireActivity(), object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToHomeScreen()
            }
        })
    }
    private fun navigateToHomeScreen() {
        val intent = Intent(context, MainActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }

    override fun selectedItem(item: String?) {
        TODO("Not yet implemented")
    }
}