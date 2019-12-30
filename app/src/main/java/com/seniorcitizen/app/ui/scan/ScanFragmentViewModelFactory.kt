package com.seniorcitizen.app.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-20.
 */
class ScanFragmentViewModelFactory@Inject constructor(private val scanFragmentViewModel: ScanViewModel): ViewModelProvider.Factory{
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(ScanViewModel::class.java)){
			return scanFragmentViewModel as T
		}
		throw IllegalArgumentException("Unknown class name")
	}
}
