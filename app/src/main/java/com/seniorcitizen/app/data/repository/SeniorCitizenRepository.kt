package com.seniorcitizen.app.data.repository

import com.seniorcitizen.app.data.model.AppAuthenticateRequest
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.model.LoginRequest
import com.seniorcitizen.app.data.model.RegisterRequest
import com.seniorcitizen.app.data.model.RegisterResponse
import com.seniorcitizen.app.data.model.RegisterWithImageRequest
import com.seniorcitizen.app.data.model.Transaction
import com.seniorcitizen.app.data.model.UpdateUserRequest
import com.seniorcitizen.app.data.model.UpdateUserResponse
import com.seniorcitizen.app.data.model.UserTransactionRequest
import com.seniorcitizen.app.data.remote.ApiInterface
import com.seniorcitizen.app.persistence.dao.SeniorCitizenDao
import com.seniorcitizen.app.utils.Constants
import com.seniorcitizen.app.utils.Utils
import io.reactivex.Observable
import io.reactivex.Single
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
    var request: AppAuthenticateRequest = AppAuthenticateRequest()

    fun getSeniorLogin(user: String, pw: String): Observable<List<Entity.SeniorCitizen>> {

        return getSenior(user, pw)
    }

    fun getAllSenior(appToken: String): Observable<List<Entity.SeniorCitizen>> {

        // val hasConnection = utils.isConnectedToInternet()

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

    fun registerUser(request: RegisterRequest):Observable<RegisterResponse>{

        val observer : Observable<RegisterResponse> = regUser(request)
        return observer
    }

    fun registerUserWithImage(request: RegisterRequest, image : String?):Observable<RegisterResponse>{

        val observer: Observable<RegisterResponse>?
        if (image!=null){
            val reg = RegisterWithImageRequest(
                Address = request.Address,
                Username = request.Username,
                Password = request.Password,
                FirstName = request.FirstName,
                LastName = request.LastName,
                MiddleName = request.MiddleName,
                Sex = request.Sex,
                Birthday = request.Birthday,
                IsPWD = request.IsPWD,
                IDNumber = request.IDNumber,
                IsSenior = request.IsSenior,
                SeniorImage = image
            )
            Timber.i("$reg")
            observer = regUserWithImage(reg)

        }else{
            observer = regUser(request)
        }

        return observer
    }

    fun updateUser(request : UpdateUserRequest): Observable<UpdateUserResponse>{
        val observer: Observable<UpdateUserResponse>?
        observer = update(request)
        return observer
    }

    fun loginAccount(username: String,password: String): Observable<LoginRequest>{
        val observer: Observable<LoginRequest>?

        observer = login(username,password)

        return observer.doOnNext { account ->

            val cit = Entity.SeniorCitizen(
                firstName = account.firstName,
                middleName = account.middleName,
                lastName = account.lastName,
                birthday = account.birthday,
                address = account.address,
                isSenior = account.isSenior,
                isPWD = account.isPWD,
                isActive = account.isActive,
                modifiedDate = account.modifiedDate,
                creationDate = account.creationDate,
                seniorCitizenID = account.seniorCitizenID,
                seniorImage = account.seniorImage,
                sex = account.sex,
                username = account.username,
                password = account.password,
                idNumber = account.idNumber
            )
            seniorCitizenDao.insertSeniorCitizen(cit)
        }
    }

    fun updateUserWithImage(request: RegisterRequest, image : String?):Observable<RegisterResponse>{

        val observer: Observable<RegisterResponse>?
        if (image!=null){
            val reg = RegisterWithImageRequest(
                Address = request.Address,
                Username = request.Username,
                Password = request.Password,
                FirstName = request.FirstName,
                LastName = request.LastName,
                MiddleName = request.MiddleName,
                Sex = request.Sex,
                Birthday = request.Birthday,
                IsPWD = request.IsPWD,
                IDNumber = request.IDNumber,
                IsSenior = request.IsSenior,
                SeniorImage = image
            )
            Timber.i("$reg")
            observer = regUserWithImage(reg)

        }else{
            observer = regUser(request)
        }

        return observer
    }


    fun getAllTransactions(user: UserTransactionRequest): Single<List<Transaction>> {

        val observableTransactionApi: Single<List<Transaction>> = getTransactionBySeniorCitizenID(Constants.APP_TOKEN,user)

        return observableTransactionApi
    }

    fun getTransactionById(id: Int): Single<List<Transaction>>{
        val single : Single<List<Transaction>> = getTransactionByTransactionid(Constants.APP_TOKEN,id)
        return single
    }

    private fun getTransactionBySeniorCitizenID(token: String,id: UserTransactionRequest): Single<List<Transaction>>{
        return apiInterface.getUserTransactions("Bearer "+token,id.SeniorCitizenID!!)
    }

    private fun getTransactionByTransactionid(token: String,id: Int): Single<List<Transaction>>{
        return apiInterface.getTransactionByTransationId("Bearer "+token,id)
    }

    fun authenticateApp(username: String, password: String): Observable<AppAuthenticateResponse> {
        Timber.i("$username $password")

        val hasConnection = utils.isConnectedToInternet()

        var observableAppAuth: Observable<AppAuthenticateResponse>? = null

        request = AppAuthenticateRequest(username = username, password = password)

        if (hasConnection) {
            observableAppAuth = apiInterface.authenticateApp(request)
                .doOnNext {
                    it.token?.let { it1 ->
                    // set member variable
                    Constants.APP_TOKEN = it.token
                    // get api call
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

        return apiInterface.getAllSenior("Bearer $token")
            .doOnNext {
                if(it.isNotEmpty()){
                    seniorCitizenDao.purgeUsers()
                    for (item in it) {
                        Timber.i("insertSeniorCitizen:%s",item.firstName)
                        seniorCitizenDao.insertSeniorCitizen(item)
                    }
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

        // val test = SimpleCrypto.decrypt(BuildConfig.SECRET_KEY,p)
        return seniorCitizenDao.getSeniorCitizen(u, p)
            .doOnNext {
                Timber.e(it.size.toString())
            }
    }

    fun getSeniorById(id: String): Observable<List<Entity.SeniorCitizen>> {
        return seniorCitizenDao.getSeniorCitizenByIdNumber(id)
            .doOnNext {
                Timber.e(it.size.toString())
            }
    }

    private fun regUser(request: RegisterRequest): Observable<RegisterResponse>{
        return apiInterface.registerUser("Bearer "+Constants.APP_TOKEN,request)
    }

    private fun regUserWithImage(request: RegisterWithImageRequest): Observable<RegisterResponse>{
        return apiInterface.registerWithImageUser(
            "Bearer "+Constants.APP_TOKEN,
            request
        )
    }

    private fun update(request: UpdateUserRequest): Observable<UpdateUserResponse>{
        return apiInterface.updateUser(
            "Bearer "+Constants.APP_TOKEN,
            request
        )
    }

    private fun login(username: String,password: String): Observable<LoginRequest>{
        return apiInterface.loginUser(
            "Bearer "+Constants.APP_TOKEN,
            username,
            password
        )
    }
}