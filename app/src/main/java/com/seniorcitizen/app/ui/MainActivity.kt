package com.seniorcitizen.app.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.AppAuthenticateResponse
import com.seniorcitizen.app.data.model.SeniorCitizen
import com.seniorcitizen.app.ui.login.LoginActivity
import com.seniorcitizen.app.ui.register.RegisterActivity
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mainActivityViewModelFactory: MainActivityViewModelFactory
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        mainActivityViewModel = ViewModelProviders.of(this,mainActivityViewModelFactory).get(
            MainActivityViewModel::class.java)

        btn_to_login.setOnClickListener { startActivity<LoginActivity>() }
        btn_to_register.setOnClickListener { startActivity<RegisterActivity>() }

        progress_bar.visibility = View.VISIBLE

        getAuth()

        mainActivityViewModel.appAuthenticateResult().observe(this, Observer<AppAuthenticateResponse>{
            if (it!=null){
                Timber.e(it.toString())
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

        loadAllSenior()

        mainActivityViewModel.seniorCitizenResult().observe(this, Observer<List<SeniorCitizen>>{
            if (it!=null){
                Timber.e(it.size.toString())
            }
        })

        mainActivityViewModel.seniorCitizenError().observe(this, Observer<String> {
            if (it!=null){
                toast(resources.getString(R.string.error_401) + it)
            }
        })

        mainActivityViewModel.seniorCitizenLoader().observe(this, Observer<Boolean>{
            if(it == false) progress_bar.visibility = View.GONE
        })

    }

    private fun getAuth(){
        mainActivityViewModel.loadToken()
    }

    private fun loadAllSenior(){
      mainActivityViewModel.loadSeniorCitizens()
    }

    override fun onDestroy() {
        mainActivityViewModel.disposeElements()

        super.onDestroy()
    }
}
