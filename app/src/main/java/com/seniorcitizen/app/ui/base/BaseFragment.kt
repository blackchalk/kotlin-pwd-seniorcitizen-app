package com.seniorcitizen.app.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-20.
 */
abstract class BaseFragment<VDB : ViewDataBinding, VM : ViewModel> : Fragment() {
	@Inject
	lateinit var viewModelFactory: ViewModelProvider.Factory

	open lateinit var mBinding: VDB

	fun init(inflater: LayoutInflater, container: ViewGroup) {
		mBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
	}

	open fun init() {}

	@LayoutRes
	protected abstract fun getLayoutRes(): Int

	override fun onCreate(@Nullable savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		AndroidSupportInjection.inject(this)
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
		savedInstanceState: Bundle?): View {
		init(inflater, container!!)
		init()
		super.onCreateView(inflater, container, savedInstanceState)
		return mBinding.root
	}
}
