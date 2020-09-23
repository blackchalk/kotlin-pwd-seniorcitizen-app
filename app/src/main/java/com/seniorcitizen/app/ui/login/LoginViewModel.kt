package com.seniorcitizen.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.model.LoginRequest
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import com.seniorcitizen.app.utils.Validator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Alvin Raygon on 2019-12-10.
 */
class LoginViewModel @Inject constructor(private val seniorCitizenRepository: SeniorCitizenRepository, private val validator: Validator) : ViewModel(){

    private lateinit var loginCallback: LoginCallback
    fun init(loginCallback: LoginCallback) {
        this.loginCallback = loginCallback
    }

    var loginAccountResult: MutableLiveData<LoginRequest> = MutableLiveData()
    var loginAccountError: MutableLiveData<String> = MutableLiveData()
    var loginAccountLoading: MutableLiveData<Boolean> = MutableLiveData()

    fun loginAccountResult(): LiveData<LoginRequest> = loginAccountResult
    fun loginAccountError(): LiveData<String> = loginAccountError
    fun loginAccountLoading(): LiveData<Boolean> = loginAccountLoading

    private lateinit var dispoableLoginObserver: DisposableObserver<LoginRequest>

    private val _onProgressBar = MutableLiveData<Boolean>()
    fun onProgressBar() = _onProgressBar as LiveData<Boolean>

    fun doLoginOnline(user : Entity.SeniorCitizen){

            _onProgressBar.value = true

            dispoableLoginObserver = object: DisposableObserver<LoginRequest>(){

                override fun onComplete() {
                    Timber.i("onComplete")
                    loginAccountLoading.postValue(false)
                    _onProgressBar.postValue(false)
                }

                override fun onNext(t: LoginRequest) {
                    // prevent the app from proceeding if response has null values for key objects
                    if(t.seniorCitizenID != null && t.idNumber != null){
                        loginAccountResult.value = t
                        loginCallback.onSuccess()

                    }else{
                        loginCallback.onFailure("Login Failed")
                    }
                }

                override fun onError(e: Throwable) {

                    loginAccountLoading.postValue(false)
                    _onProgressBar.postValue(false)
                    loginCallback.onFailure(e.message)
                }
            }

            seniorCitizenRepository.loginAccount(user.username!!,user.password!!)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribe(dispoableLoginObserver)
    }

    private fun contentFillValidate(user: Entity.SeniorCitizen): Boolean {
        return (Validator.isValidName(user.username.toString(),false)
            && Validator.isValidPassword(user.password.toString(),false))
    }

    fun disposeElements(){

        if(this::dispoableLoginObserver.isInitialized){
            if(!dispoableLoginObserver.isDisposed){
                dispoableLoginObserver.dispose()
            }
        }
    }

    fun getLoggedInUser() : String? {
        // return seniorCitizenResult.value?.first()?.idNumber.toString()
        val number = loginAccountResult.value?.idNumber.toString()
        Timber.i(number)
        return number
    }
}
