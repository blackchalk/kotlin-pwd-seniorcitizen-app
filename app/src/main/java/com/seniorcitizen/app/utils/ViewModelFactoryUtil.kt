package com.seniorcitizen.app.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * Created by Nic Evans on 2019-12-10.
 */
class ViewModelFactoryUtil@Inject constructor(private val viewModel: MutableMap<Class<out ViewModel>, Provider<ViewModel>>)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = viewModel[modelClass]?.get() as T

}