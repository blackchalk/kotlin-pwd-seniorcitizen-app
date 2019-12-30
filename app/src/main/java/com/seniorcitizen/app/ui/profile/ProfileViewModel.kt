package com.seniorcitizen.app.ui.profile

import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-20.
 */
class ProfileViewModel@Inject constructor(val seniorCitizenRepository: SeniorCitizenRepository) : ViewModel()  {

    fun test(){
        Timber.i("test")

    }
}
