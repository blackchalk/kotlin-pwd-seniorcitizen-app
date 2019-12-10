package com.seniorcitizen.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-10.
 */
class MainActivityViewModelFactory @Inject constructor(private val mainActivityViewModel: MainActivityViewModel): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)){
            return mainActivityViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}