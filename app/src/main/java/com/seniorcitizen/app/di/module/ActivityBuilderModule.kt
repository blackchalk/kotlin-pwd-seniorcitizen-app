package com.seniorcitizen.app.di.module

import com.seniorcitizen.app.ui.MainActivity
import com.seniorcitizen.app.ui.login.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Nic Evans on 2019-12-10.
 */
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

}