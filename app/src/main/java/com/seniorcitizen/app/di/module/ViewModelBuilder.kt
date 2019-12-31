package com.seniorcitizen.app.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seniorcitizen.app.di.scope.ViewModelKey
import com.seniorcitizen.app.ui.MainActivityViewModel
import com.seniorcitizen.app.ui.home.HomeActivityViewModel
import com.seniorcitizen.app.ui.login.LoginViewModel
import com.seniorcitizen.app.ui.profile.ProfileViewModel
import com.seniorcitizen.app.ui.scan.ScanViewModel
import com.seniorcitizen.app.ui.transaction.TransactionViewModel
import com.seniorcitizen.app.utils.ViewModelFactoryUtil
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Nic Evans on 2019-12-11.
 */
@Suppress("unused")
@Module
abstract class ViewModelBuilder {

    @Binds
    internal abstract fun bindViewModelFactory(viewModelFactoryUtil: ViewModelFactoryUtil): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(loginViewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    internal abstract fun bindMainViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    internal abstract fun bindHomeViewModel(homeActivityViewModel: HomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProfileViewModel::class)
    internal abstract fun bindProfileViewModel(profileViewModel: ProfileViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TransactionViewModel::class)
    internal abstract fun bindTransactionViewModel(transactionViewModel: TransactionViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ScanViewModel::class)
    internal abstract fun bindScanViewModel(scanViewModel: ScanViewModel): ViewModel

}