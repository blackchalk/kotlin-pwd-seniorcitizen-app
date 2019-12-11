package com.seniorcitizen.app.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.seniorcitizen.app.di.scope.ViewModelKey
import com.seniorcitizen.app.ui.MainActivityViewModel
import com.seniorcitizen.app.ui.login.LoginViewModel
import com.seniorcitizen.app.utils.ViewModelFactoryUtil
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * Created by Nic Evans on 2019-12-11.
 */
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


}