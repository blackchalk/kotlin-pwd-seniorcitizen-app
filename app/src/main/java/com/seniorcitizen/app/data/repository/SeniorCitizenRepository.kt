package com.seniorcitizen.app.data.repository

import com.seniorcitizen.app.data.model.AppAuthenticateRequest
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.remote.ApiInterface
import com.seniorcitizen.app.persistence.dao.SeniorCitizenDao
import com.seniorcitizen.app.utils.Utils
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject


/**
 * Created by Nic Evans on 2019-12-10.
 */

class SeniorCitizenRepository @Inject constructor(
    private val seniorCitizenDao: SeniorCitizenDao
    , private val apiInterface: ApiInterface
    , private val utils: Utils
) {

    var request: AppAuthenticateRequest? = null

    fun getSeniorLogin(user: String, pw: String): Observable<List<Entity.SeniorCitizen>> {

        return getSenior(user, pw)
    }

    fun getAllSenior(appToken: String): Observable<List<Entity.SeniorCitizen>> {

        val hasConnection = utils.isConnectedToInternet()

        val observableFromApi: Observable<List<Entity.SeniorCitizen>>?

        // if (hasConnection) {
            observableFromApi = getSeniorCitizensFromApi(appToken)
        // }
        // val observableFromDb = getSeniorCitizenFromDb()
        //
        // return if (hasConnection) Observable.concatArrayEager(observableFromApi, observableFromDb)
        // else observableFromDb
        return observableFromApi
    }

    fun authenticateApp(username: String, password: String): Observable<AppAuthenticateResponse> {

        val hasConnection = utils.isConnectedToInternet()

        var observableAppAuth: Observable<AppAuthenticateResponse>? = null

        request = AppAuthenticateRequest(username = username, password = password)

        if (hasConnection) {
            observableAppAuth = apiInterface.authenticateApp(request!!)
                .doOnNext { it.token?.let { it1 ->
                    getAllSenior(it1)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .subscribe()
                } }
        }
        return observableAppAuth!!

    }

    private fun getSeniorCitizensFromApi(token : String): Observable<List<Entity.SeniorCitizen>> {

        return apiInterface.getAllSenior("Bearer " + token)
            .doOnNext {
                for (item in it) {
                    Timber.i("insertSeniorCitizen:%s",item.firstName)
                    seniorCitizenDao.insertSeniorCitizen(item)
                }
            }
    }

    private fun getSeniorCitizenFromDb(): Observable<List<Entity.SeniorCitizen>> {
        return seniorCitizenDao.getAllSeniorCitizen()
            .toObservable()
            .doOnNext {
                Timber.e(it.size.toString())
            }
    }

    private fun getSenior(u: String, p: String): Observable<List<Entity.SeniorCitizen>> {
        return seniorCitizenDao.getSeniorCitizen(u, p)
            .doOnNext {
                Timber.e(it.size.toString())
            }
    }

    private fun getSeniorByID(id: String): Observable<List<Entity.SeniorCitizen>> {
        return seniorCitizenDao.getSeniorCitizenByIdNumber(id)
            .doOnNext {
                Timber.e(it.size.toString())
            }
    }
    //
    // private fun getTransactions(token: String): Observable<List<Transaction>>{
    //     return apiInterface.getUserTransactions()
    // }
}