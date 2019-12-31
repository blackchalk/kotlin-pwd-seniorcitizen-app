package com.seniorcitizen.app.ui.profile

import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-20.
 */
class ProfileViewModel@Inject constructor(val seniorCitizenRepository: SeniorCitizenRepository) : ViewModel()  {

    private var fullName = "Test T Something 123 sample .."

    fun setUser(model: Entity.SeniorCitizen){
        val builder = model.firstName.plus(" "+model.middleName).plus(" "+model.lastName)
        this.fullName = builder
    }

    fun getUserFullName() : String {
        val t = this.fullName
        return t
    }

}
