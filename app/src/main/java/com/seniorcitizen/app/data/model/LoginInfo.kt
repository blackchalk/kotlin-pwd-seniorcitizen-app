package com.seniorcitizen.app.data.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
/**
 * Created by Nic Evans on 2019-12-10.
 */
class LoginInfo: BaseObservable() {
    var userName: String? = null
        @Bindable get() = field
        set(userName) {
            field = userName
            notifyPropertyChanged(BR.userName)
        }
    var userPassWord: String? = null
        @Bindable get() = field
        set(userPassWord) {
            field = userPassWord
            notifyPropertyChanged(BR.userPassWord)
        }
}