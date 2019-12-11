package com.seniorcitizen.app.data.repository

import com.seniorcitizen.app.data.model.AppAuthenticateRequest
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.data.model.SeniorCitizen
import com.seniorcitizen.app.data.remote.ApiInterface
import com.seniorcitizen.app.persistence.dao.SeniorCitizenDao
import com.seniorcitizen.app.utils.Constants
import com.seniorcitizen.app.utils.Utils
import io.reactivex.Observable
import timber.log.Timber
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

    fun getSeniorLogin(user: String, pw: String): Observable<List<SeniorCitizen>> {

        return getSenior(user, pw)
    }

    fun getAllSenior(): Observable<List<SeniorCitizen>> {
        val hasConnection = utils.isConnectedToInternet()
        var observableFromApi: Observable<List<SeniorCitizen>>? = null
        if (hasConnection) {
            observableFromApi = getSeniorCitizensFromApi()
        }
        val observableFromDb = getSeniorCitizenFromDb()

        return if (hasConnection) Observable.concatArrayEager(observableFromApi, observableFromDb)
        else observableFromDb
    }

    fun authenticateApp(username: String, password: String): Observable<AppAuthenticateResponse> {
        val hasConnection = utils.isConnectedToInternet()
        var observableAppAuth: Observable<AppAuthenticateResponse>? = null

        request = AppAuthenticateRequest(username = username, password = password)

        if (hasConnection) {
            observableAppAuth = apiInterface.authenticateApp(request!!)
                .doOnNext {
                    if (it != null) {
                        Constants.APP_TOKEN = it.token.toString()
                    }
                }
        }
        return observableAppAuth!!

    }

    private fun getSeniorCitizensFromApi(): Observable<List<SeniorCitizen>> {
        return apiInterface.getAllSenior("Bearer " + Constants.APP_TOKEN)
            .doOnNext {
                Timber.e(it.size.toString())
                for (item in it) {
                    seniorCitizenDao.insertSeniorCitizen(item)
                }
            }
    }

    private fun getSeniorCitizenFromDb(): Observable<List<SeniorCitizen>> {
        return seniorCitizenDao.getAllSeniorCitizen()
            .toObservable()
            .doOnNext {
                Timber.e(it.size.toString())
            }
    }

    private fun getSenior(u: String, p: String): Observable<List<SeniorCitizen>> {
        return seniorCitizenDao.getSeniorCitizen(u, p)
            .doOnNext {
                Timber.e(it.size.toString())
            }
    }
}