package com.seniorcitizen.app.ui.login

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.databinding.ActivityLoginBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.ui.home.HomeActivity
import com.seniorcitizen.app.ui.register.RegisterActivity
import com.seniorcitizen.app.utils.Constants
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import timber.log.Timber

class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginCallback{

    private val viewModel: LoginViewModel by viewModels {
        viewModelFactory
    }

    override fun getContentView(): Int = R.layout.activity_login

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.apply { init(this@LoginActivity) }

        getBinding()?.let {
            it.viewModel = viewModel
            it.user = Entity.SeniorCitizen()
        }

        viewModel.seniorCitizenResult().observe(this,Observer<List<Entity.SeniorCitizen>>{
            if (it!=null){
                Timber.e(it.size.toString())
            }
        })

        viewModel.seniorCitizenError().observe(this, Observer<String> {
            if (it!=null){
                toast(resources.getString(R.string.error_401) + it)
            }
        })

        viewModel.seniorCitizenLoader().observe(this, Observer<Boolean>{
            // if(it == false) progress_bar.visibility = View.GONE
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        iv_back.setOnClickListener {
            // pop to last stack
            finish()
        }
        tv_to_register.setOnClickListener { startActivity<RegisterActivity>() }    }

    override fun onSuccess() {
        onToastMessage(getString(R.string.login_success))
        toHomePage()
    }

    override fun onFailure(responseMessage: String?) {
        onToastMessage(responseMessage)
    }

    private fun toHomePage() {
        startActivity<HomeActivity>(Constants.INTENT_KEY to getBinding()?.viewModel?.getLoggedInUser())
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.disposeElements()
    }
}
