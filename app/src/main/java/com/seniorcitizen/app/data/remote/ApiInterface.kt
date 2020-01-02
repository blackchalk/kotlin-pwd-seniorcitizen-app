package com.seniorcitizen.app.data.remote

import com.seniorcitizen.app.data.model.AppAuthenticateRequest
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.model.Transaction
import com.seniorcitizen.app.data.model.UserTransactionRequest
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

/**
 * Created by Nic Evans on 2019-12-10.
 */

interface ApiInterface {
    @Headers(
        "Accept: application/json",
        "Content-type: application/json"
    )
    @POST("api/transaction/authenticate")
    fun authenticateApp(@Body request: AppAuthenticateRequest): Observable<AppAuthenticateResponse>

    @GET("api/senior/get")
    fun getAllSenior(@Header("Authorization") authToken: String): Observable<List<Entity.SeniorCitizen>>

    //TODO api to be fixed
    // @HTTP(method = "GET", path = "api/transaction/get", hasBody = true)
    @GET("api/transaction/get")
    fun getUserTransactions(
        @Header("Authorization") authToken: String
        , @Body userTransactionRequest: UserTransactionRequest
    ): Single<List<Transaction>>
}