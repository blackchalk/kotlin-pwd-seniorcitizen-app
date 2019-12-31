package com.seniorcitizen.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import com.seniorcitizen.app.ui.login.LoginCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-10.
 */
class MainActivityViewModel @Inject constructor(private val seniorCitizenRepository: SeniorCitizenRepository) : ViewModel() {

    private lateinit var loginCallback: LoginCallback
    fun init(loginCallback: LoginCallback) {
        this.loginCallback = loginCallback
    }
    var seniorCitizenResult: MutableLiveData<List<Entity.SeniorCitizen>> = MutableLiveData()
    var seniorCitizenError: MutableLiveData<String> = MutableLiveData()
    var seniorCitizenLoader: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var disposableObserver: DisposableObserver<List<Entity.SeniorCitizen>>

    fun seniorCitizenResult(): LiveData<List<Entity.SeniorCitizen>> = seniorCitizenResult
    fun seniorCitizenError(): LiveData<String> = seniorCitizenError
    fun seniorCitizenLoader(): LiveData<Boolean> = seniorCitizenLoader

    var appResult: MutableLiveData<AppAuthenticateResponse> = MutableLiveData()
    var appError: MutableLiveData<String> = MutableLiveData()
    var appLoader: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var disposableAuthenticateObserver: DisposableObserver<AppAuthenticateResponse>

    fun appAuthenticateResult(): LiveData<AppAuthenticateResponse> = appResult
    fun appError(): LiveData<String> = seniorCitizenError
    fun appLoader(): LiveData<Boolean> = seniorCitizenLoader

    fun loadToken(){
        disposableAuthenticateObserver = object : DisposableObserver<AppAuthenticateResponse>(){
            override fun onComplete() {
                Timber.i("onComplete")
            }

            override fun onNext(t: AppAuthenticateResponse) {
                appResult.postValue(t)
                appLoader.postValue(false)
                Timber.i("onNext %s",t)
            }

            override fun onError(e: Throwable) {
                appError.postValue(e.message)
                appLoader.postValue(false)
                Timber.i("onError %s",e.message)
            }
        }

        seniorCitizenRepository.authenticateApp("test","password")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400,TimeUnit.MILLISECONDS)
            .subscribe(disposableAuthenticateObserver)
    }

    fun loadSeniorCitizens(){

        disposableObserver = object : DisposableObserver<List<Entity.SeniorCitizen>>(){
            override fun onComplete() {

            }

            override fun onNext(t: List<Entity.SeniorCitizen>) {
                seniorCitizenResult.postValue(t)
                seniorCitizenLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
                seniorCitizenError.postValue(e.message)
                seniorCitizenLoader.postValue(false)
            }
        }

        seniorCitizenRepository.getAllSenior("sample")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400,TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun disposeElements(){
        // if(!disposableObserver.isDisposed) disposableObserver.dispose()
        if(!disposableAuthenticateObserver.isDisposed) disposableAuthenticateObserver.dispose()
    }
}