package com.seniorcitizen.app.ui.transaction

import com.seniorcitizen.app.data.model.Transaction

/**
 * Created by Nic Evans on 2020-01-02.
 */
interface TransactionSelectedListener {
    fun onTransactionSelected(transaction: Transaction?)
}