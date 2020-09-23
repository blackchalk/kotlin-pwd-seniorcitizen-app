package com.seniorcitizen.app.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Created by Alvin Raygon on 2019-12-20.
 */
class TransactionFragmentViewModelFactory@Inject constructor(private val transactionFragmentViewModel: TransactionViewModel): ViewModelProvider.Factory{
	override fun <T : ViewModel?> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(TransactionViewModel::class.java)){
			return transactionFragmentViewModel as T
		}
		throw IllegalArgumentException("Unknown class name")
	}
}
