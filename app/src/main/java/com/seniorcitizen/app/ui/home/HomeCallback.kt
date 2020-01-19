package com.seniorcitizen.app.ui.home

import com.seniorcitizen.app.ui.base.BaseCallback

/**
 * Created by Nic Evans on 2019-12-31.
 */
interface HomeCallback: BaseCallback {
    fun onSuccess(message: String)
}