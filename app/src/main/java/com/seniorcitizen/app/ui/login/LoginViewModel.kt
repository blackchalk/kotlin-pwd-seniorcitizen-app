package com.seniorcitizen.app.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
class LoginViewModel @Inject constructor(private val seniorCitizenRepository: SeniorCitizenRepository) : ViewModel(){

    private lateinit var loginCallback: LoginCallback
    fun init(loginCallback: LoginCallback) {
        this.loginCallback = loginCallback
    }
    var seniorCitizenResult: MutableLiveData<List<SeniorCitizen>> = MutableLiveData()
    var seniorCitizenError: MutableLiveData<String> = MutableLiveData()
    var seniorCitizenLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun seniorCitizenResult(): LiveData<List<SeniorCitizen>> = seniorCitizenResult
    fun seniorCitizenError(): LiveData<String> = seniorCitizenError
    fun seniorCitizenLoader(): LiveData<Boolean> = seniorCitizenLoader

    private lateinit var disposableObserver: DisposableObserver<List<SeniorCitizen>>

    private val _onProgressBar = MutableLiveData<Boolean>()
    fun onProgressBar() = _onProgressBar as LiveData<Boolean>

    private val _isErrorUserName = MutableLiveData<Boolean>()
    fun isErrorUserName() = _isErrorUserName as LiveData<Boolean>

    private val _isErrorPassword = MutableLiveData<Boolean>()
    fun isErrorPassword() = _isErrorPassword as LiveData<Boolean>

    // fun watchFieldEmail(text: CharSequence?) { _isErrorEmail.value = !Patterns.EMAIL_ADDRESS.matcher(text).matches() }
    // fun watchFieldPassword(text: CharSequence?) { _isErrorPassword.value = text.let { it != null && it.isEmpty() } }

    fun doLogin(user : SeniorCitizen){
        if (contentFillValidate(user)){
            _onProgressBar.value = true

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

            user.username?.let { user.password?.let { it1 ->
                seniorCitizenRepository.getSeniorLogin(it,
                    it1)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .debounce(400, TimeUnit.MILLISECONDS)
                    .subscribe(disposableObserver) } }
            }
    }

    private fun contentFillValidate(user: SeniorCitizen): Boolean {
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
        if(!disposableObserver.isDisposed) disposableObserver.dispose()
    }
}