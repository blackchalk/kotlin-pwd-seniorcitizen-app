package com.seniorcitizen.app.ui.profile

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.databinding.FragmentProfileBinding
import com.seniorcitizen.app.ui.base.BaseFragment
import com.seniorcitizen.app.ui.home.HomeActivityViewModel
import kotlinx.android.synthetic.main.fragment_profile.*


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

	private val profileViewModel: ProfileViewModel by viewModels {
		viewModelFactory
	}

	override fun getLayoutRes(): Int = R.layout.fragment_profile

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		mBinding.let {
			it.viewmodel = homeActivityViewModel
			it.user = Entity.SeniorCitizen()
			it.viewmodelProfile = profileViewModel
		}

		activity?.let {
			homeActivityViewModel?.seniorCitizenResult?.observe(
				it,
				Observer<List<Entity.SeniorCitizen>> {
					if (it.isNotEmpty()) {
						val item = it[0]

						tv_name.text = item.firstName + " " +item.middleName +" " +item.lastName
						tv_address.text = item.address
						tv_birthday.text = homeActivityViewModel?.formatBirthday(item.birthday)
						tv_sex.text = item.sex
						tv_age.text = homeActivityViewModel?.getAge(item.birthday)
						tv_id_number.text = item.idNumber
					}
				})
		}
	}
}
