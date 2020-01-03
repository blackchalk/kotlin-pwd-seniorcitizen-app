package com.seniorcitizen.app.ui.scan

import androidx.lifecycle.ViewModel
import com.seniorcitizen.app.data.model.Transaction
import com.seniorcitizen.app.data.repository.SeniorCitizenRepository
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

/**
 * Created by Nic Evans on 2019-12-20.
 */
class ScanViewModel@Inject constructor(val seniorCitizenRepository: SeniorCitizenRepository) : ViewModel()  {

    private var disposable: CompositeDisposable? = null

    fun getTransactionById(id: Int): Single<List<Transaction>> {

       return seniorCitizenRepository.getTransactionById(id)

    }
}
