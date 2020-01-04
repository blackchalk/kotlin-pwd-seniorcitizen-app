package com.seniorcitizen.app.data.remote

import com.seniorcitizen.app.data.model.AppAuthenticateRequest
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.model.RegisterRequest
import com.seniorcitizen.app.data.model.RegisterResponse
import com.seniorcitizen.app.data.model.Transaction
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Created by Nic Evans on 2019-12-10.
 */

interface ApiInterface {
    @Headers(
        "Accept: application/json",
        "Content-type: application/json"
    )
    @POST("api/transaction/authenticate")
    fun authenticateApp(
        @Body request: AppAuthenticateRequest
    ): Observable<AppAuthenticateResponse>

    @GET("api/senior/get")
    fun getAllSenior(
        @Header("Authorization") authToken: String
    ): Observable<List<Entity.SeniorCitizen>>

    @GET("api/transaction/get")
    fun getUserTransactions(
        @Header("Authorization") authToken: String
        , @Query("seniorCitizenId") id: Int
    ): Single<List<Transaction>>

    @GET("api/transaction/getbyid")
    fun getTransactionByTransationId(
        @Header("Authorization") authToken: String
        , @Query("transactionid") id: Int
    ): Single<List<Transaction>>

    @POST("api/senior/create")
    fun registerUser(
        @Header("Authorization") authToken: String,
        @Body request: RegisterRequest
    ): Observable<RegisterResponse>

}