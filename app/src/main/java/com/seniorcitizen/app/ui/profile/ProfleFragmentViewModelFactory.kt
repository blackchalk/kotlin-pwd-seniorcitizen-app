package com.seniorcitizen.app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Created by Alvin Raygon on 2019-12-20.
 */
class ProfleFragmentViewModelFactory@Inject constructor(private val profileFragmentViewModel: ProfileViewModel): ViewModelProvider.Factory{
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(ProfileViewModel::class.java)){
			return profileFragmentViewModel as T
		}
		throw IllegalArgumentException("Unknown class name")
	}
}
