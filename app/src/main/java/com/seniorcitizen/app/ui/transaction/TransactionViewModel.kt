package com.seniorcitizen.app.ui.transaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.model.Transaction
import com.seniorcitizen.app.data.model.UserTransactionRequest
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Alvin Raygon on 2019-12-20.
 */
class TransactionViewModel @Inject constructor(val seniorCitizenRepository: SeniorCitizenRepository) : ViewModel()  {

    private var disposable: CompositeDisposable? = null

    private val transactions: MutableLiveData<List<Transaction>> = MutableLiveData()

    private val transactionsLoadError: MutableLiveData<String> = MutableLiveData()

    private val loading: MutableLiveData<Boolean> = MutableLiveData()

    private var request : UserTransactionRequest? = null

    fun getTransactions(): LiveData<List<Transaction>> {
        return transactions
    }

    fun getError(): LiveData<String> {
        return transactionsLoadError
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun initTransactions(user: Entity.SeniorCitizen){
        fetchTransactions(user)
    }

    // handle the fetching of the users' transactions
    private fun fetchTransactions(user: Entity.SeniorCitizen){

        loading.value = true

        request = UserTransactionRequest(SeniorCitizenID = user.seniorCitizenID)

        disposable = CompositeDisposable()

        disposable!!.add(
            seniorCitizenRepository.getAllTransactions(request!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object :
                                DisposableSingleObserver<List<Transaction>>() {

                                override fun onSuccess(value: List<Transaction>) {
                                    transactions.postValue(value)
                                    loading.postValue(false)
                                }

                                override fun onError(e: Throwable) {
                                    transactionsLoadError.postValue(e.message)
                                    loading.postValue(false)
                                }
                            }))
    }

    override fun onCleared() {
        super.onCleared()
        if (disposable != null) {
            disposable!!.clear()
            disposable = null
        }
    }

    // fun clearElements(){
    //     disposable?.clear()
    // }
    //
    // fun disposeElements(){
    //     if(!disposable!!.isDisposed){ disposable?.dispose()}
    // }
}
