package com.seniorcitizen.app.ui.login

/**
 * Created by Alvin Raygon on 2019-12-10.
 */
interface LoginCallback {
    fun onSuccess()
    fun onFailure(responseMessage: String?)
}
