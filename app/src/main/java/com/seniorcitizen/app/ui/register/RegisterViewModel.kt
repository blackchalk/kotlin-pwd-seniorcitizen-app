package com.seniorcitizen.app.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import com.seniorcitizen.app.utils.Validator
import javax.inject.Inject

/**
 * Created by Nic Evans on 2020-01-03.
 */
class RegisterViewModel @Inject constructor(private val seniorCitizenRepository: SeniorCitizenRepository, private val validator: Validator) : ViewModel(){

    private lateinit var registerCallback: RegisterCallback
    fun init(registerCallback: RegisterCallback){
        this.registerCallback = registerCallback
    }

    private val _onProgressBar = MutableLiveData<Boolean>()
    fun onProgressBar() = _onProgressBar as LiveData<Boolean>

}