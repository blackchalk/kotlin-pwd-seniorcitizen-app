package com.seniorcitizen.app.ui.register

import android.os.Bundle
import androidx.activity.viewModels
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.databinding.ActivityRegisterBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity

class RegisterActivity: BaseActivity<ActivityRegisterBinding>(), RegisterCallback {

    private val viewModel: RegisterViewModel by viewModels {
        viewModelFactory
    }
    override fun getContentView(): Int = R.layout.activity_register

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        viewModel.apply { init(this@RegisterActivity) }

        getBinding()?.let {
            it.viewModel = viewModel
            it.user = Entity.SeniorCitizen()
        }

        iv_back.setOnClickListener {
            // pop to last stack
            finish()
        }
        tv_to_login.setOnClickListener { toLoginPage() }
    }

    override fun onSuccess() {
        onToastMessage(getString(R.string.registration_success))
        toLoginPage()
    }

    override fun onFailure(responseMessage: String, responseCode: Int) {
        if (responseCode == 406)
            onToastMessage(getString(R.string.error_registration_account_with_same_email_already_exist))
        else
            onToastMessage(responseMessage)
    }

    private fun toLoginPage() {
        startActivity<LoginActivity>()
        finish()
    }
}