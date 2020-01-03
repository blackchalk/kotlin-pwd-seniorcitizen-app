package com.seniorcitizen.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import com.seniorcitizen.app.utils.Validator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-10.
 */
class LoginViewModel @Inject constructor(private val seniorCitizenRepository: SeniorCitizenRepository, private val validator: Validator) : ViewModel(){

    private lateinit var loginCallback: LoginCallback
    fun init(loginCallback: LoginCallback) {
        this.loginCallback = loginCallback
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

    private val _isErrorUserName = MutableLiveData<Boolean>()
    fun isErrorUserName() = _isErrorUserName as LiveData<Boolean>

    private val _isErrorPassword = MutableLiveData<Boolean>()
    fun isErrorPassword() = _isErrorPassword as LiveData<Boolean>

    fun watchFieldUserName(text: CharSequence?) { _isErrorUserName.value = text?.let { validator.isValidName(it) } }
    fun watchFieldPassword(text: CharSequence?) { _isErrorPassword.value = text?.let { validator.isValidPassword(it) }
    }

    fun doLogin(user : Entity.SeniorCitizen){
        if (contentFillValidate(user)){
            _onProgressBar.value = true

            disposableObserver = object : DisposableObserver<List<Entity.SeniorCitizen>>(){
                override fun onComplete() {
                }

                override fun onNext(t: List<Entity.SeniorCitizen>) {
                    seniorCitizenResult.postValue(t)
                    seniorCitizenLoader.postValue(false)
                    _onProgressBar.postValue(false)

                    if(t.isNotEmpty()){
                        loginCallback.onSuccess()
                        getLoggedInUser()
                    }else{
                        loginCallback.onFailure("No User Found.")
                    }

                }

                override fun onError(e: Throwable) {

                    seniorCitizenError.postValue(e.message)
                    seniorCitizenLoader.postValue(false)
                    _onProgressBar.postValue(false)
                    loginCallback.onFailure(e.message)

                }

            }

            user.username?.let { user.password?.let { it1 ->
                seniorCitizenRepository.getSeniorLogin(it,
                    it1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .doOnComplete { disposableObserver.dispose() }
                    .subscribe(disposableObserver) } }
            }
    }

    private fun contentFillValidate(user: Entity.SeniorCitizen): Boolean {
        if (user.username.let { it != null})
            if (user.password.let { it != null && it.isNotEmpty() })
                return true
            else
                _isErrorPassword.value = true
        else
            _isErrorUserName.value = true

        return false
    }

    fun disposeElements(){

    }

    fun getLoggedInUser() : String? {
        return seniorCitizenResult.value?.first()?.idNumber.toString()
    }
}