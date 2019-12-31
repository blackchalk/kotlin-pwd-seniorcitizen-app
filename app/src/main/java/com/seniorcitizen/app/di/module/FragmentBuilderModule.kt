package com.seniorcitizen.app.di.module

import com.seniorcitizen.app.ui.profile.ProfileFragment
import com.seniorcitizen.app.ui.scan.ScanFragment
import com.seniorcitizen.app.ui.transaction.TransactionFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Nic Evans on 2019-12-20.
 */
@Suppress("unused")
@Module
abstract class FragmentBuilderModule {

	@ContributesAndroidInjector
	abstract fun contributeProfileFragment(): ProfileFragment

	@ContributesAndroidInjector
	abstract fun contributeTransactionFragment(): TransactionFragment

	@ContributesAndroidInjector
	abstract fun contributeScanFragment(): ScanFragment
}
