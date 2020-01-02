package com.seniorcitizen.app.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
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
    fun setUserIdNumber(number: String) { this.userIDNumber = number }

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
        if (userIDNumber.isNotEmpty()){

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
            val str = userIDNumber
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

    fun formatBirthday(stringDate: String?): String{
        // convert birthday
        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)//model.birthday.toString(), Locale.US
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)

        return formatter.format(parser.parse(stringDate))

    }

    fun getAge(
        stringDate: String?
    ): Int? {

        val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
        val date = parser.parse(stringDate!!)

        val year: Int? = date?.year
        val month: Int? = date?.month
        val day: Int? = date?.day

        //calculating age from dob
        val dob: Calendar = Calendar.getInstance()
        val today: Calendar = Calendar.getInstance()
        dob.set(year!!, month!!, day!!)
        var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--
        }
        Timber.i("%s/year-%s,month-%s,day-%s",date,year,month,day)
        Timber.i("%s",age)
        return age
    }
}
