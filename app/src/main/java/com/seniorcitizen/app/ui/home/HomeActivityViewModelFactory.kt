package com.seniorcitizen.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Created by Alvin Raygon on 2019-12-20.
 */
class HomeActivityViewModelFactory @Inject constructor(private val homeActivityViewModel: HomeActivityViewModel): ViewModelProvider.Factory{
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(HomeActivityViewModel::class.java)){
			return homeActivityViewModel as T
		}
		throw IllegalArgumentException("Unknown class name")
	}
}
