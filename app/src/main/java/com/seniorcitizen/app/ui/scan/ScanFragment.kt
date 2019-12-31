package com.seniorcitizen.app.ui.scan

import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Handler
import com.afollestad.materialdialogs.MaterialDialog
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.seniorcitizen.app.R
import com.seniorcitizen.app.databinding.FragmentScanBinding
import com.seniorcitizen.app.ui.base.BaseFragment
import com.seniorcitizen.app.utils.Utils
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by Nic Evans on 2019-12-20.
 */
class ScanFragment: BaseFragment<FragmentScanBinding, ScanViewModel>(), QRCodeReaderView.OnQRCodeReadListener{

	@Inject
	lateinit var viewmodel: ScanViewModel
	@Inject
	lateinit var utils: Utils

	private lateinit var qrdecoderview: QRCodeReaderView

	override fun getLayoutRes() = R.layout.fragment_scan

	override fun init() {
		super.init()

		val vw = mBinding.root

		qrdecoderview = vw.findViewById(R.id.qrdecoderview) as QRCodeReaderView

		initializeQRScanner()
	}

	@SuppressLint("RestrictedApi")
	override fun onQRCodeRead(text: String?, points: Array<out PointF>?) {
		qrdecoderview.setQRDecodingEnabled(false)

		try{
			val handler = Handler()
			handler.postDelayed({
				//5 ms delay
			}, 500)

			Timber.d("onQRCodeRead: %s", text)

			//TODO: do api call to get the transaction id

				context?.let {
					val mD = MaterialDialog(it)
						.title(R.string.ScanDialogTitle)
						.show {
							message( text = text.toString())
							positiveButton(text = "OK") { dialog ->
								dialog.dismiss()
								qrdecoderview.setQRDecodingEnabled(true)
							}
							negativeButton(text = "Close") { dialog ->
								dialog.dismiss()
								qrdecoderview.setQRDecodingEnabled(true)
							}
						}
					mD.show()
				}

		}catch (e: Exception){

			qrdecoderview.setQRDecodingEnabled(false)
			e.printStackTrace()

			context?.let {
				val mD = MaterialDialog(it)
					.title(R.string.ScanDialogFailedTitle)
					.show {
						message( text = e.message?.plus("\nPlease try again."))
						positiveButton(text = "OK") { dialog ->
							dialog.dismiss()
							qrdecoderview.setQRDecodingEnabled(true)
						}
					}
				mD.show()
			}
		}

	}

	private fun initializeQRScanner() {
		try {
            qrdecoderview.setOnQRCodeReadListener(this)

            // Use this function to enable/disable decoding
            qrdecoderview.setQRDecodingEnabled(true)

            // Use this function to change the autofocus interval (default is 5 secs)
            qrdecoderview.setAutofocusInterval(2000L)

            // Use this function to enable/disable Torch
            qrdecoderview.setTorchEnabled(false)

            // Use this function to set front camera preview
            qrdecoderview.setFrontCamera()

            // Use this function to set back camera preview
            qrdecoderview.setBackCamera()
		}catch (ex : Exception){
			ex.printStackTrace()
		}
	}

	override fun onPause() {
		super.onPause()
		qrdecoderview.setQRDecodingEnabled(false)
	}

	override fun onResume() {
		super.onResume()
		qrdecoderview.setQRDecodingEnabled(true)
	}

	override fun onDestroyView() {
		super.onDestroyView()
		qrdecoderview.stopCamera()
	}
}
