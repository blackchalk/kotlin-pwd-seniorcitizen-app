package com.seniorcitizen.app.utils

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.seniorcitizen.app.R
import javax.inject.Inject


/**
 * Created by Alvin Raygon on 2019-12-10.
 */
class Utils @Inject constructor(private val context: Context) {

    fun checkPermission(
        context: Context,
        activity: Activity,
        permission: String,
        request_code: Int
    ): Boolean {
        return if (ContextCompat.checkSelfPermission(context, permission) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(permission),
                request_code
            )
            false
        } else {
            true
        }
    }

    fun isConnectedToInternet(): Boolean {
        val connectivity = context.getSystemService(
            Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivity.allNetworkInfo
        for (i in info.indices)
            if (info[i].state == NetworkInfo.State.CONNECTED) {
                return true
            }
        return false
    }

    abstract class showDialogAction(
        context: Context?,
        title: String?,
        message: String?
    ) {
        abstract fun onClickOk()
        abstract fun onClickCancel()

        init {
            if (context != null) {
                AlertDialog.Builder(context, R.style.MyDialogTheme)
                    .setTitle(title)
                    .setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.ok, DialogInterface.OnClickListener { dialog, which ->
                        // dismiss dialog
                        onClickOk()
                        dialog.dismiss()
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialog, which ->
                        onClickCancel()
                        dialog.dismiss()
                    }).create()
                    .show()
            }
        }
    }

    public abstract class showDialogOK(
        context: Context?,
        title: String?,
        message: String?
    ) {
        abstract fun onClickOk()

        init {
            if (context != null) {
                AlertDialog.Builder(context, R.style.MyDialogTheme)
                    .setTitle(title)
                    .setMessage(message)
                    .setOnDismissListener(DialogInterface.OnDismissListener { onClickOk() })
                    .setPositiveButton(
                        android.R.string.ok,
                        DialogInterface.OnClickListener { dialog, which ->
                            // dismiss dialog
                            onClickOk()
                            dialog.dismiss()
                        }).create()
                    .show()
            }
        }
    }
}
