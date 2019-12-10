package com.seniorcitizen.app.di.component

import com.seniorcitizen.app.App
import com.seniorcitizen.app.di.module.AppModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

/**
 * Created by Nic Evans on 2019-12-09.
 */
@Singleton
@Component(
    modules = [(AndroidInjectionModule::class),(AppModule::class)]
)
interface AppComponent {
    fun inject(app : App)
}