package com.seniorcitizen.app.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.RegisterRequest
import com.seniorcitizen.app.data.model.RegisterResponse
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import com.seniorcitizen.app.utils.Constants
import com.seniorcitizen.app.utils.Utils
import com.seniorcitizen.app.utils.Validator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * Created by Nic Evans on 2020-01-03.
 */
class RegisterViewModel @Inject constructor(
    private val seniorCitizenRepository: SeniorCitizenRepository,
    private val validator: Validator,
    private val utils: Utils
) : ViewModel() {

    private var disposable: CompositeDisposable? = null

    private lateinit var registerCallback: RegisterCallback

    fun init(registerCallback: RegisterCallback) {
        this.registerCallback = registerCallback
    }

    private val _onProgressBar = MutableLiveData<Boolean>()
    fun onProgressBar() = _onProgressBar as LiveData<Boolean>

    private val registerLiveData: MutableLiveData<RegisterResponse> = MutableLiveData()
    fun getRegisterLiveData(): LiveData<RegisterResponse> = registerLiveData

    private val registerLiveError: MutableLiveData<String> = MutableLiveData()
    fun getRegisterLiveError() = registerLiveError as LiveData<String>

    fun doRegister(user: RegisterRequest?, image: String?) {

        disposable = CompositeDisposable()

        _onProgressBar.value = true

        val disposableObserver = object : DisposableObserver<RegisterResponse>() {
            override fun onComplete() {
                Timber.i("onComplete")
                _onProgressBar.postValue(false)
                registerCallback.onSuccess()
            }

            override fun onNext(t: RegisterResponse) {

                registerLiveData.postValue(t)

                disposable?.add(
                    seniorCitizenRepository.getAllSenior(Constants.APP_TOKEN)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .subscribe()
                )
            }

            override fun onError(e: Throwable) {
                _onProgressBar.postValue(false)
                registerLiveError.postValue(e.message)
                registerCallback.onFailure(e.message.toString(), 400)
            }
        }
        // seniorCitizenRepository.registerUser(user!!)
        seniorCitizenRepository.registerUserWithImage(user!!, image)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .debounce(400, TimeUnit.MILLISECONDS)
            .doOnComplete { disposableObserver.dispose() }
            .subscribe(disposableObserver)

        disposable?.add(disposableObserver)
    }

    fun disposeElements() {

        if (disposable != null) {
            if (disposable?.isDisposed == false) {
                disposable?.dispose()
            }
        }
    }
}