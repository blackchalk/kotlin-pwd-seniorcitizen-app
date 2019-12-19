package com.seniorcitizen.app.di.component

import android.app.Application
import com.seniorcitizen.app.App
import com.seniorcitizen.app.di.module.ActivityBuilderModule
import com.seniorcitizen.app.di.module.AppModule
import com.seniorcitizen.app.di.module.NetModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * Created by Nic Evans on 2019-12-09.
 */
@Singleton
@Component(
    modules =
    [(AndroidInjectionModule::class),
        (AndroidSupportInjectionModule::class),
        (ActivityBuilderModule::class),
        (AppModule::class),
        (NetModule::class)]
)
@SuppressWarnings("unchecked")
interface AppComponent : AndroidInjector<App> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent

    }

}