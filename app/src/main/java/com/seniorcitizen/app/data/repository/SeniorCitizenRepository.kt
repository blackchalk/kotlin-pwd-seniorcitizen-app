package com.seniorcitizen.app.data.repository

import android.util.Base64
import com.seniorcitizen.app.BuildConfig
import com.seniorcitizen.app.data.model.AppAuthenticateRequest
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.model.RegisterRequest
import com.seniorcitizen.app.data.model.RegisterResponse
import com.seniorcitizen.app.data.model.Transaction
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
import java.io.IOException
import java.security.InvalidAlgorithmParameterException
import java.security.InvalidKeyException
import java.security.KeyStoreException
import java.security.NoSuchAlgorithmException
import java.security.NoSuchProviderException
import java.security.SignatureException
import java.security.UnrecoverableEntryException
import java.util.concurrent.TimeUnit
import javax.crypto.BadPaddingException
import javax.crypto.IllegalBlockSizeException
import javax.crypto.NoSuchPaddingException
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

                for (item in it) {

                    // encrypt sensitive data
                    // val rawMsg = item.password!!

                    // val converted = conceal(rawMsg)

                    // val test = SimpleCrypto.encrypt(BuildConfig.SECRET_KEY,rawMsg)

                    // item.password = test
                    // proceed insert to db
                    Timber.i("insertSeniorCitizen:%s",item.firstName)
                    seniorCitizenDao.insertSeniorCitizen(item)
                }
            }
    }

    private fun conceal(textToConceal: String): String{
        try {
            val encryptedText: ByteArray = utils.enCryptInstance().encryptText(
                BuildConfig.SECRET_KEY,
                textToConceal
            )

            return Base64.encodeToString(encryptedText, Base64.DEFAULT)
        } catch (e: UnrecoverableEntryException) {
            Timber.e(e)
        } catch (e: NoSuchAlgorithmException) {
            Timber.e(e)
        } catch (e: NoSuchProviderException) {
            Timber.e(e)
        } catch (e: KeyStoreException) {
            Timber.e(e)
        } catch (e: IOException) {
            Timber.e(e)
        } catch (e: NoSuchPaddingException) {
            Timber.e(e)
        } catch (e: InvalidKeyException) {
            Timber.e(e)
        } catch (e: InvalidAlgorithmParameterException) {
            e.printStackTrace()
        } catch (e: SignatureException) {
            e.printStackTrace()
        } catch (e: IllegalBlockSizeException) {
            e.printStackTrace()
        } catch (e: BadPaddingException) {
            e.printStackTrace()
        }
        return textToConceal //returning the raw message
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

    fun comparePassword(u: String, p: String) {
        //try to encrypt p
        val converted = conceal(p)

        val disposable = attemptLogin(u)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe({ data ->
                //decrypt data

                // val encrypted = Base64.decode(data[0].password, Base64.DEFAULT);
                // utils.deCryptInstance().decryptData(BuildConfig.SECRET_KEY,encrypted,encrypted.get(0))
                Timber.i("check if match")
                Timber.i("database password: %s",data[0].password)
                Timber.i("user input password: $converted")

            },{
                    err ->
                    Timber.e(err)
            })

        disposable.dispose()
    }

    private fun attemptLogin(u: String) : Observable<List<Entity.SeniorCitizen>>{
        return seniorCitizenDao.attemptLoginWithUserName(u)
    }

    fun getSeniorById(id: String): Observable<List<Entity.SeniorCitizen>> {
        return seniorCitizenDao.getSeniorCitizenByIdNumber(id)
            .doOnNext {
                Timber.e(it.size.toString())
            }
    }

    private fun regUser(request: RegisterRequest): Observable<RegisterResponse>{
        Timber.i("adding token:")
        Timber.i("Bearer "+Constants.APP_TOKEN)
        return apiInterface.registerUser("Bearer "+Constants.APP_TOKEN,request)
    }

}