package com.seniorcitizen.app.ui.login

/**
 * Created by Nic Evans on 2019-12-10.
 */
interface LoginCallback {
    fun onSuccess()
    fun onFailure(responseMessage: String?)
}