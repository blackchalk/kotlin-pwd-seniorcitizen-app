package com.seniorcitizen.app.ui.adapter

import android.app.Activity
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.seniorcitizen.app.utils.gone
import com.seniorcitizen.app.utils.invisible
import com.seniorcitizen.app.utils.visible
import java.util.function.Consumer


object BindingAdapter {

    @JvmStatic
    @BindingAdapter("onProgressBar")
    fun onProgressBar(view: View, onProgressBar: Boolean?) {
        if (onProgressBar.let { it != null && it == true }) {
            view.visible()

            val activity = view.context as Activity
            activity.window.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        } else {
            view.gone()

            val activity = view.context as Activity
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)

        }
    }

    @JvmStatic
    @BindingAdapter("isErrorField")
    fun isErrorField(view: View, isErrorField: Boolean?) {
        if (isErrorField.let { it != null && it == true })  view.visible() else view.invisible()
    }

    @BindingAdapter("onTextChanged")
    fun bindTextWatcher(editText: TextInputEditText, onTextChanged: Consumer<CharSequence>) {
        editText.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) { // No-op.
                }

                override fun onTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        onTextChanged.accept(charSequence.toString())
                    }
                }

                override fun afterTextChanged(editable: Editable) { // No-op.
                }
            })
    }

}