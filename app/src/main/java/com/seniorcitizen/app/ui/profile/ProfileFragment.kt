package com.seniorcitizen.app.ui.profile

import android.os.Bundle
import com.seniorcitizen.app.R
import com.seniorcitizen.app.databinding.FragmentProfileBinding
import com.seniorcitizen.app.ui.base.BaseFragment
import com.seniorcitizen.app.ui.home.HomeActivityViewModel
import javax.inject.Inject


/**
 * Created by Nic Evans on 2019-12-20.
 */

class ProfileFragment: BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
	@Inject
	lateinit var viewModel: ProfileViewModel

	@Inject
	lateinit var vmodel : HomeActivityViewModel

	override fun getLayoutRes(): Int = R.layout.fragment_profile

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		vmodel.test()
	}
}
