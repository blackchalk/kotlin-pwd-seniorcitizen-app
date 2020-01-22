package com.seniorcitizen.app.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.model.LoginRequest
import com.seniorcitizen.app.databinding.ActivityLoginBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.ui.home.HomeActivity
import com.seniorcitizen.app.ui.register.RegisterActivity
import com.seniorcitizen.app.utils.Constants
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
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

        viewModel.loginAccountResult().observe(this,Observer<LoginRequest>{
            if(it!=null){
                Timber.i(it.firstName)
            }
        })

        viewModel.loginAccountError().observe(this, Observer<String> {
            if (it!=null){
                toast(resources.getString(R.string.error_401) + it)
            }
        })

        viewModel.loginAccountLoading().observe(this, Observer<Boolean>{
            if (it){
                btn_login.isEnabled = false
                progress_bar.visibility = View.VISIBLE
            }else{
                btn_login.isEnabled = true
                progress_bar.visibility = View.GONE
            }
        })

        var isUserNameValid = true
        var isUserPasswordValid = true

        val userNameWatcher = object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                isUserNameValid = et_username.validator()
                    .nonEmpty()
                    .minLength(2)
                    .addErrorCallback {
                        ti_username.error = it
                    }
                    .addSuccessCallback {

                        ti_username.error = null
                    }
                    .check()
            }
        }

        val passwordWatcher = object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                isUserPasswordValid = et_password.validator()
                    .nonEmpty()
                    .minLength(8)
                    .atleastOneUpperCase()
                    .atleastOneLowerCase()
                    .atleastOneNumber()
                    .atleastOneSpecialCharacters()
                    .addErrorCallback {

                        ti_password.error = it
                        ti_password.errorIconDrawable = null
                    }
                    .addSuccessCallback {

                        ti_password.error = null
                    }
                    .check()
            }
        }

        et_password.addTextChangedListener(passwordWatcher)
        et_username.addTextChangedListener(userNameWatcher)

        btn_login.setOnClickListener {

            if (isUserNameValid && isUserPasswordValid){

                // attempt to login
                viewModel.doLoginOnline(
                    Entity.SeniorCitizen(
                        username = et_username.text.toString(),
                        password = et_password.text.toString()
                    )
                )
            }
        }
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
