package com.seniorcitizen.app.ui.profile

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.seniorcitizen.app.R
import com.seniorcitizen.app.databinding.FragmentProfileBinding
import com.seniorcitizen.app.ui.base.BaseFragment
import com.seniorcitizen.app.ui.home.HomeActivityViewModel
import timber.log.Timber


/**
 * Created by Nic Evans on 2019-12-20.
 */

class ProfileFragment: BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

	// uses parent activity view model
	private val homeActivityViewModel by lazy {
		activity?.let {
			ViewModelProviders.of(it, viewModelFactory).get(HomeActivityViewModel::class.java)
		}
	}

	override fun getLayoutRes(): Int = R.layout.fragment_profile

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		val test = homeActivityViewModel?.currentUser()
		Timber.e("%s",test)
	}
}
