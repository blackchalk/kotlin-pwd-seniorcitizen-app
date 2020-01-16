package com.seniorcitizen.app.ui

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.databinding.ActivityMainBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.ui.login.LoginActivity
import com.seniorcitizen.app.ui.register.RegisterActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import timber.log.Timber
import javax.inject.Inject

class MainActivity : BaseActivity<ActivityMainBinding>(){

    @Inject
    lateinit var mainActivityViewModel : MainActivityViewModel
    private lateinit var disposable : Disposable
    override fun getContentView(): Int = R.layout.activity_main

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        btn_to_login.setOnClickListener { startActivity<LoginActivity>() }
        btn_to_register.setOnClickListener { startActivity<RegisterActivity>() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermission()

        try{
            progress_bar.visibility = View.VISIBLE
            getAppAuthAndUsers()

        }catch(e: Exception){
            Timber.e(e)
        }

        mainActivityViewModel.appAuthenticateResult().observe(this, Observer<AppAuthenticateResponse>{
            if (it!=null){
                progress_bar.visibility = View.GONE
            }else{
                progress_bar.visibility = View.VISIBLE
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
    }

    // Authenticate the app and get users
    private fun getAppAuthAndUsers(){
        mainActivityViewModel.loadToken()
    }

    override fun onDestroy() {
        super.onDestroy()
        mainActivityViewModel.disposeElements()
    }

    // asks user for permissions for camera and read/write storage
    private fun checkPermission() {
        val rxPermissions = RxPermissions(this) // where this is an Activity or Fragment instance
        // Must be done during an initialization phase like onCreate
        disposable = rxPermissions
            .request(
                Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA)
            .subscribe { granted: Boolean ->
                if (granted) { // Always true pre-M

                } else { // Oups permission denied
                    finish()
                }
            }
    }
}
