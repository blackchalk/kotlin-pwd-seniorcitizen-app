package com.seniorcitizen.app.ui.scan

import com.seniorcitizen.app.data.model.Transaction

/**
 * Created by Alvin Raygon on 2019-12-10.
 */
interface ScanCallback {
    fun onSuccess(transaction: Transaction)
    fun onFailure(responseMessage: String, responseCode: Int)
}
