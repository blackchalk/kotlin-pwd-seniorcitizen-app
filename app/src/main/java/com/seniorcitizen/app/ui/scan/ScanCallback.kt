package com.seniorcitizen.app.ui.register

/**
 * Created by Nic Evans on 2019-12-10.
 */
interface RegisterCallback {
    fun onSuccess()
    fun onFailure(responseMessage: String, responseCode: Int)
}