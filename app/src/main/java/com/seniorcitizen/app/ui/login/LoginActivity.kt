package com.seniorcitizen.app.ui.login

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.SeniorCitizen
import com.seniorcitizen.app.databinding.ActivityLoginBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import timber.log.Timber

class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginCallback{

    private val viewModel by lazy {
        ViewModelProviders.of(this@LoginActivity, viewModelFactory)[LoginViewModel::class.java]
            .apply { init(this@LoginActivity) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btn_login.setOnClickListener {
            progress_bar.visibility = View.VISIBLE
            login()
        }

        viewModel.seniorCitizenResult().observe(this,Observer<List<SeniorCitizen>>{
            if (it!=null){
                Timber.e(it.size.toString())
                progress_bar.visibility = View.GONE
            }
        })

        viewModel.seniorCitizenError().observe(this, Observer<String> {
            if (it!=null){
                toast(resources.getString(R.string.error_401) + it)
            }
        })

        viewModel.seniorCitizenLoader().observe(this, Observer<Boolean>{
            if(it == false) progress_bar.visibility = View.GONE
        })
    }

    private fun login(){
        viewModel.doLogin(SeniorCitizen( username = et_username?.text.toString(),password = et_password?.text.toString()))
    }

    override fun getContentView(): Int = R.layout.activity_login

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        iv_back.setOnClickListener {
            // startActivity<MainActivity>()
            finish()
        }
        tv_to_register.setOnClickListener { startActivity<RegisterActivity>() }    }

    override fun onSuccess() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onFailure(responseMessage: String?, responseCode: Int) {
        when (responseCode) {

            401 -> {
                onToastMessage(getString(R.string.error_401))
            }

            406 -> onToastMessage(getString(R.string.error_login_wrong_password))

            400 -> onToastMessage(getString(R.string.error_login_email_is_not_registered))

            else -> onToastMessage(responseMessage)
    }
}
}
