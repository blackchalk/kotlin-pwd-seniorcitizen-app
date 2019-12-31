package com.seniorcitizen.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-19.
 */
class HomeActivityViewModel@Inject constructor(private val seniorCitizenRepository: SeniorCitizenRepository) : ViewModel() {

    private lateinit var homeCallback: HomeCallback

    fun init(homeCallback: HomeCallback) {
        this.homeCallback = homeCallback
    }
    private lateinit var userIDNumber: String
    fun getUserIdNumber(number: String) { this.userIDNumber = number }

    fun currentUser() : String{
        return userIDNumber
    }

    var seniorCitizenResult: MutableLiveData<List<Entity.SeniorCitizen>> = MutableLiveData()
    var seniorCitizenError: MutableLiveData<String> = MutableLiveData()
    var seniorCitizenLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun seniorCitizenResult(): LiveData<List<Entity.SeniorCitizen>> = seniorCitizenResult
    fun seniorCitizenError(): LiveData<String> = seniorCitizenError
    fun seniorCitizenLoader(): LiveData<Boolean> = seniorCitizenLoader

    private lateinit var disposableObserver: DisposableObserver<List<Entity.SeniorCitizen>>


    private val _onProgressBar = MutableLiveData<Boolean>()
    fun onProgressBar() = _onProgressBar as LiveData<Boolean>

    private val _isNoMatchFound = MutableLiveData<Boolean>()
    fun isNoMatchFound() = _isNoMatchFound as LiveData<Boolean>

    fun doRequetUser(){
        if (currentUser().isNotEmpty()){

            _onProgressBar.value = true

            disposableObserver = object: DisposableObserver<List<Entity.SeniorCitizen>>() {
                override fun onComplete() {
                    _onProgressBar.postValue(false)
                }

                override fun onNext(t: List<Entity.SeniorCitizen>) {
                    seniorCitizenResult.postValue(t)
                    seniorCitizenLoader.postValue(false)
                    _onProgressBar.postValue(false)

                    if (t.isNotEmpty()){
                        homeCallback.onSuccess()
                    }else{
                        homeCallback.onToastMessage("No result found.")
                    }
                }

                override fun onError(e: Throwable) {
                    seniorCitizenError.postValue(e.message)
                    seniorCitizenLoader.postValue(false)
                    _onProgressBar.postValue(false)
                }

            }
            val str = currentUser()
            seniorCitizenRepository.getSeniorById(str)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .doOnComplete{ disposableObserver.dispose() }
                .subscribe(disposableObserver)
        }
    }

    fun disposeElements(){
        if(!disposableObserver.isDisposed) disposableObserver.dispose()
    }
}
