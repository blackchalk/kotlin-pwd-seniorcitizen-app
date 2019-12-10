package com.seniorcitizen.app.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.data.model.SeniorCitizen
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-10.
 */
class MainActivityViewModel @Inject constructor(private val seniorCitizenRepository: SeniorCitizenRepository) : ViewModel() {
    var seniorCitizenResult: MutableLiveData<List<SeniorCitizen>> = MutableLiveData()
    var seniorCitizenError: MutableLiveData<String> = MutableLiveData()
    var seniorCitizenLoader: MutableLiveData<Boolean> = MutableLiveData()
    private lateinit var disposableObserver: DisposableObserver<List<SeniorCitizen>>

    fun seniorCitizenResult(): LiveData<List<SeniorCitizen>> = seniorCitizenResult
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

            }

            override fun onNext(t: AppAuthenticateResponse) {
                appResult.postValue(t)
                appLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
                appError.postValue(e.message)
                appLoader.postValue(false)
            }
        }

        seniorCitizenRepository.authapp("test","12345")
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400,TimeUnit.MILLISECONDS)
            .subscribe(disposableAuthenticateObserver)
    }

    fun loadSeniorCitizens(){

        disposableObserver = object : DisposableObserver<List<SeniorCitizen>>(){
            override fun onComplete() {

            }

            override fun onNext(t: List<SeniorCitizen>) {
                seniorCitizenResult.postValue(t)
                seniorCitizenLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
                seniorCitizenError.postValue(e.message)
                seniorCitizenLoader.postValue(false)
            }
        }

        seniorCitizenRepository.getAllSenior()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400,TimeUnit.MILLISECONDS)
            .subscribe(disposableObserver)
    }

    fun disposeElements(){
        if(!disposableObserver.isDisposed) disposableObserver.dispose()
        if(!disposableAuthenticateObserver.isDisposed) disposableAuthenticateObserver.dispose()
    }
}