package com.seniorcitizen.app.di.module

import com.seniorcitizen.app.ui.MainActivity
import com.seniorcitizen.app.ui.home.HomeActivity
import com.seniorcitizen.app.ui.login.LoginActivity
import com.seniorcitizen.app.ui.register.RegisterActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Nic Evans on 2019-12-10.
 */
@Suppress("unused")
@Module(
    includes = [
        ViewModelBuilder::class
    ]
)
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    abstract fun contributeHomeActivity(): HomeActivity

    abstract fun contributeRegisterActivity(): RegisterActivity

}