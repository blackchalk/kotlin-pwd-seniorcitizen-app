package com.seniorcitizen.app.ui.register

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.RegisterRequest
import com.seniorcitizen.app.databinding.ActivityRegisterBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.ui.login.LoginActivity
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.startActivity
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RegisterActivity: BaseActivity<ActivityRegisterBinding>(), RegisterCallback {

    private val viewModel: RegisterViewModel by viewModels {
        viewModelFactory
    }

    override fun getContentView(): Int = R.layout.activity_register

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        viewModel.apply { init(this@RegisterActivity) }

        getBinding()?.let {
            it.viewModel = viewModel
            it.user = RegisterRequest()
        }

        viewModel.getResponse().observe(this, Observer { response ->
            if (response!=null){
                Timber.i("success \n$response")
            }
        })

        viewModel.onProgressBar().observe(this, Observer { loading ->
            if (loading) {
                btn_register.isEnabled = false
                progress_bar.visibility = View.VISIBLE
            }else{
                btn_register.isEnabled = true
                progress_bar.visibility = View.GONE
            }
        })

        iv_back.setOnClickListener {
            // pop to last stack
            finish()
        }

        tv_to_login.setOnClickListener { toLoginPage() }

        handleBirthdayDatePick()
    }

    private fun handleBirthdayDatePick() {

        et_birthday.hint = "MM-dd-yyyy"

        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val selectedDate = sdf.format(cal.time)
                et_birthday.setText(selectedDate)
            }

        et_birthday.setOnClickListener {
            val dpd = DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            dpd.datePicker.maxDate = System.currentTimeMillis()
            dpd.show()
        }
    }

    fun onRadioButtonStatusClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_senior ->
                    if (checked) {
                        getBinding()?.user?.IsSenior = true
                        getBinding()?.user?.IsPWD = false
                    }
                R.id.radio_pwd ->
                    if (checked) {
                        getBinding()?.user?.IsSenior = false
                        getBinding()?.user?.IsPWD = true
                    }
            }
        }
    }

    fun onRadioButtonSexClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_male ->
                    if (checked) {
                        getBinding()?.user?.Sex = "Male"
                    }
                R.id.radio_female ->
                    if (checked) {
                        getBinding()?.user?.Sex = "Female"
                    }
            }
        }
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disposeElements()
    }
}