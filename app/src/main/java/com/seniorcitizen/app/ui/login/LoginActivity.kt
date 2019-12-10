package com.seniorcitizen.app.ui.login

import android.os.Bundle
import com.seniorcitizen.app.R
import com.seniorcitizen.app.databinding.ActivityLoginBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.ui.register.RegisterActivity
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity

class LoginActivity : BaseActivity<ActivityLoginBinding>(), LoginCallback{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
