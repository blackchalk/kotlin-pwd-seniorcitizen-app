package com.seniorcitizen.app.ui.home

import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-19.
 */
class HomeActivityViewModel@Inject constructor(private val seniorCitizenRepository: SeniorCitizenRepository) : ViewModel() {

    fun test(){
        Timber.i("HomeActivityViewModel")

    }
}
