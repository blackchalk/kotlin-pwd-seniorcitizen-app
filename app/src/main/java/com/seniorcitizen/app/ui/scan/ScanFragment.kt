package com.seniorcitizen.app.ui.scan

import android.annotation.SuppressLint
import android.graphics.PointF
import android.os.Handler
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.list.listItems
import com.dlazaro66.qrcodereaderview.QRCodeReaderView
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.Transaction
import com.seniorcitizen.app.databinding.FragmentScanBinding
import com.seniorcitizen.app.ui.base.BaseFragment
import com.seniorcitizen.app.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import org.jetbrains.anko.toast
import timber.log.Timber
import javax.inject.Inject


/**
 * Created by Nic Evans on 2019-12-20.
 */
class ScanFragment: BaseFragment<FragmentScanBinding, ScanViewModel>(), QRCodeReaderView.OnQRCodeReadListener, ScanCallback{

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
			}, 100)

			Timber.d("onQRCodeRead: %s", text)

			if (text!=null) {
				val disposable = CompositeDisposable()

				disposable.add(viewmodel.getTransactionById(text.toInt())
					            .subscribeOn(Schedulers.io())
					            .observeOn(AndroidSchedulers.mainThread())
					            .subscribeWith(object :
					                DisposableSingleObserver<List<Transaction>>() {

					                override fun onSuccess(value: List<Transaction>) {
										val create = ArrayList<CharSequence>()

										val detaillist = value[0].transactionDetail
										if (detaillist != null) {
											for(names in detaillist)
											{
												create.add(" "+names?.quantity+"\t"+names?.item+"\t\t- PHP \"+names?.price+")
											}
										}

										context?.let {
											val mD = MaterialDialog(it)
												.title(R.string.ScanDialogTitle)
												.show {
													listItems( items = create)
													message( text = "ORNumber: "+value[0].orNumber+"\n" +
														"Name: "+value[0].business?.businessName+"\n" +
														"Type: "+value[0].business?.type+"\n" +
														"Total Items: "+value[0].totalQuantity
													)
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
					                }

					                override fun onError(e: Throwable) {

					                }
					            })
					        )
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

	override fun onSuccess(transaction: Transaction) {
		TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
	}

	override fun onFailure(responseMessage: String, responseCode: Int) {
		activity?.toast(resources.getString(R.string.error_401) + responseMessage)
	}
}
