package com.seniorcitizen.app.ui

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.databinding.ActivityMainBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.ui.login.LoginActivity
import com.seniorcitizen.app.ui.login.LoginCallback
import com.seniorcitizen.app.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import timber.log.Timber

class MainActivity : BaseActivity<ActivityMainBinding>(), LoginCallback {

    private val mainActivityViewModel by lazy {
        ViewModelProviders.of(this@MainActivity, viewModelFactory)[MainActivityViewModel::class.java]
            .apply { init(this@MainActivity) }
    }

    override fun getContentView(): Int = R.layout.activity_main

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        btn_to_login.setOnClickListener { startActivity<LoginActivity>() }
        btn_to_register.setOnClickListener { startActivity<RegisterActivity>() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try{
            progress_bar.visibility = View.VISIBLE
            getAppAuthAndUsers()

        }catch(e: Exception){
            Timber.e(e)
        }

        mainActivityViewModel.appAuthenticateResult().observe(this, Observer<AppAuthenticateResponse>{
            if (it!=null){
                Timber.e("%s,mainActivityViewModel",it.toString())
                progress_bar.visibility = View.GONE
            }
        })

        mainActivityViewModel.appError().observe(this, Observer<String>{
            if (it!=null){
                toast(resources.getString(R.string.error_401) + it)
            }
        })

        mainActivityViewModel.appLoader().observe(this, Observer<Boolean>{
            if(it == false) progress_bar.visibility = View.GONE
        })

        // loadAllSenior()
        //
        // mainActivityViewModel.seniorCitizenResult().observe(this, Observer<List<SeniorCitizen>>{
        //     if (it!=null){
        //         Timber.e(it.size.toString())
        //     }
        // })
        //
        // mainActivityViewModel.seniorCitizenError().observe(this, Observer<String> {
        //     if (it!=null){
        //         toast(resources.getString(R.string.error_401) + it)
        //     }
        // })
        //
        // mainActivityViewModel.seniorCitizenLoader().observe(this, Observer<Boolean>{
        //     if(it == false) progress_bar.visibility = View.GONE
        // })

    }

    private fun getAppAuthAndUsers(){
        mainActivityViewModel.loadToken()
    }

    private fun loadAllSenior(){
      mainActivityViewModel.loadSeniorCitizens()
    }

    override fun onDestroy() {
        mainActivityViewModel.disposeElements()

        super.onDestroy()
    }

    override fun onSuccess() {
        Timber.i("callback onSuccess")
    }

    override fun onFailure(responseMessage: String?, responseCode: Int) {
        Timber.i(responseMessage)
    }

}
