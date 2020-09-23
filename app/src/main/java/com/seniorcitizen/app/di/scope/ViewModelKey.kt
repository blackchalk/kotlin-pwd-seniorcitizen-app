package com.seniorcitizen.app.di.scope

import androidx.lifecycle.ViewModel
import dagger.MapKey
import kotlin.reflect.KClass

/**
 * Created by Alvin Raygon on 2019-12-11.
 */
@Retention(AnnotationRetention.RUNTIME)
@MapKey
annotation class ViewModelKey(val viewModel: KClass<out ViewModel>)
