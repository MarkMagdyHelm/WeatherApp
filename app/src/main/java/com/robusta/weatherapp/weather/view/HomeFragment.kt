package com.robusta.weatherapp.weather.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.robusta.weatherapp.R
import com.robusta.weatherapp.commons.BaseFragment
import com.robusta.weatherapp.commons.CoreDH
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModel
import com.robusta.weatherapp.weather.viewmodel.WeatherViewModelFactory
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.login.*
import javax.inject.Inject


class HomeFragment : BaseFragment() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val component by lazy { CoreDH.weatherComponent() }

    @Inject
    lateinit var viewModelFactory: WeatherViewModelFactory

    @Inject
    lateinit var viewModel: WeatherViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val root = inflater.inflate(R.layout.login, container, false)
        component.inject(this)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

//    fun bindviewmodel(view: View) {
//        viewModel.getUserDataFetchOutcome.observe(viewLifecycleOwner) { outcome ->
//            when (outcome) {
//                is Outcome.Progress ->
//                    if(outcome.loading)
//                    {  dialog!!.show()
//                    }else{ dialog!!.dismiss()
//                    }
//                is Outcome.Success -> {
//                    dialog!!.dismiss()
//                    if (outcome.data.succeeded == true) {
//                        (activity as MainActivity).showJobFailedLayout(
//                            false,
//                            ""
//                        )
//                        Constants.token = outcome.data.data?.bearerToken.toString()
//                        outcome.data.data?.phoneNumber?.let { viewModel.phonenumber.accept(it) }
//                        outcome.data.data?.email?.let { viewModel.email.accept(it) }
//                        outcome.data.data?.fullname?.let { viewModel.fullname.accept(it) }
//                        //navigate to home screen
//                        //navigate to home screen
//                        val navController = activity?.findNavController(R.id.login_host_fragment)
//                        navController?.navigate(R.id.homeFragment2, null)
//                    } else {
//                        //error
//                        (activity as MainActivity).showJobFailedLayout(
//                            true,
//                            outcome.data.errors?.get(0)?.errorMessage.toString()
//                        )
//                    }
//                }
//                is Outcome.Failure -> {
//                    dialog!!.dismiss()
//                    if (outcome.e is IOException)
//                        (activity as MainActivity).showJobFailedLayout(
//                            true,
//                            outcome.e.localizedMessage
//                        )
//                    else
//                        (activity as MainActivity).showJobFailedLayout(
//                            true,
//                            "try again later"
//                        )
//                }
//
//            }
//        }
//        view.proccedbtn.setOnClickListener { v ->
//            viewModel.login()
//        }
//        view.guestbtn.setOnClickListener { v ->
//            Constants.isGuest = true
//            //navigate to home screen
//            val navController = activity?.findNavController(R.id.login_host_fragment)
//            navController?.navigate(R.id.homeFragment2, null)
//        }
//        viewModel.loginbuttonEnabledFormFragment.subscribe {
//            Constants.isGuest = false
//            enableSubmitBtn(it, view.proccedbtn)
//        }.addTo(compositeDisposable)
//        viewModel.initloginvalidation()
//    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
        compositeDisposable.clear()
    }
}