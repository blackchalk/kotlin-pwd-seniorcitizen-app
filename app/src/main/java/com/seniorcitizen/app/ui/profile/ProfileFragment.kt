package com.seniorcitizen.app.ui.profile

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.Entity
import com.seniorcitizen.app.data.model.UpdateUserRequest
import com.seniorcitizen.app.databinding.FragmentProfileBinding
import com.seniorcitizen.app.ui.base.BaseFragment
import com.seniorcitizen.app.ui.home.HomeActivityViewModel
import com.seniorcitizen.app.utils.FileUtil
import dagger.android.support.DaggerAppCompatActivity
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile.*
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


/**
 * Created by Nic Evans on 2019-12-20.
 */

class ProfileFragment: BaseFragment<FragmentProfileBinding, ProfileViewModel>() {

	private var disposable : Disposable? = null
	// uses parent activity view model
	private val homeActivityViewModel by lazy {
		activity?.let {
			ViewModelProviders.of(it, viewModelFactory).get(HomeActivityViewModel::class.java)
		}
	}

	private val profileViewModel: ProfileViewModel by viewModels {
		viewModelFactory
	}

	private var selectedBirthday: String? = null
	private var actualImage: File? = null
	private var compressedImage: File? = null
	private var captureImgFile: File? = null
	private var selectedImageString : String? = null

	private val GALLERY = 1
	private val REQUEST_IMAGE_CAPTURE = 2
	lateinit var currentPhotoPath: String

	override fun getLayoutRes(): Int = R.layout.fragment_profile

	override fun onActivityCreated(savedInstanceState: Bundle?) {
		super.onActivityCreated(savedInstanceState)

		Timber.i("onActivityCreated")

        et_id_number.isEnabled = false

		mBinding.let {
			it.viewmodel = homeActivityViewModel
			it.user = Entity.SeniorCitizen()
			it.viewmodelProfile = profileViewModel
		}

		activity?.let {
			homeActivityViewModel?.seniorCitizenResult?.observe(
				it,
				Observer<List<Entity.SeniorCitizen>> {
					if (it.isNotEmpty()) {

						val item = it[0]

						mBinding.user = item

						selectedBirthday = item.birthday
						val formatbday = formatBirthday(item.birthday)

						et_birthday.setText(formatbday)

						if(item.sex.equals("Male")){
							rg_sex.check(R.id.radio_male)
						}else{
							rg_sex.check(R.id.radio_female)
						}

						if(item.isSenior!!){
							rg_status.check(R.id.radio_senior)
						}else{
							rg_status.check(R.id.radio_pwd)
						}

						item.seniorImage?.let {

							selectedImageString = it

							profile_img.setImageBitmap(covertImage(selectedImageString!!))
						}
					}
				})
		}

		btn_update.setOnClickListener {

			val name = et_first_name.text.toString()
			val middle = et_middle_name.text.toString()
			val last = et_last_name.text.toString()
			val birthday = selectedBirthday
			val address = et_address.text.toString()
			val pw = et_user_password.text.toString()
			val user = et_user_name.text.toString()

			val sx = if(rg_sex.checkedRadioButtonId.equals(R.id.radio_male))
			{
				"Male"
			}else{
				"Female"
			}

			var boolSenior = false
			var boolPWD = false
			if(rg_status.checkedRadioButtonId.equals(R.id.radio_senior))
			{
				boolSenior = true
				boolPWD = false
			}else{
				boolSenior = false
				boolPWD = true
			}

			val updateModel = UpdateUserRequest(
				Address = address,
				FirstName = name,
				MiddleName = middle,
				LastName = last,
				Birthday = birthday,
				Username = user,
				Password = pw,
				IDNumber = mBinding?.user?.idNumber,
				SeniorCitizenID = mBinding?.user?.seniorCitizenID,
				Sex = sx,
				IsPWD = boolPWD,
				IsSenior = boolSenior,
				SeniorImage = selectedImageString
			)

			activity?.let { activity ->
				homeActivityViewModel?.updateUser(updateModel)
			}
		}

		handleBirthdayDatePick()

		profile_img!!.setOnClickListener { showPictureDialog() }

		btn_logout.setOnClickListener {
			activity?.finish()
		}
	}

	fun formatBirthday(stringDate: String?): String{

		// convert birthday
		val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)//model.birthday.toString(), Locale.US
		val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)
		var str = ""

		try{
			val projectedAge = getAge(stringDate).toString()
			tv_age.text = projectedAge
			str = formatter.format(parser.parse(stringDate))

		}catch (io : ParseException){

			val parsemod = SimpleDateFormat("MMM dd,yyyy", Locale.US)
			val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
			selectedBirthday = format.format(parsemod.parse(stringDate))

			val projectedAge = getAge(selectedBirthday).toString()
			tv_age.text = projectedAge

			if (stringDate != null) {
				str = stringDate
			}
		}

		return str
	}

	fun formatBirthday2(stringDate: String?): String{
		// convert birthday
		val parser = SimpleDateFormat("yyyy-MM-dd", Locale.US)//model.birthday.toString(), Locale.US
		val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.US)

		return formatter.format(parser.parse(stringDate))
	}

	private fun showPictureDialog() {
		val pictureDialog = context?.let { AlertDialog.Builder(it) }
		pictureDialog?.setTitle("Select Action")
		val pictureDialogItems = arrayOf("Select image from gallery", "Capture photo from camera")
		pictureDialog?.setItems(pictureDialogItems
		) { dialog, which ->
			when (which) {
				0 -> chooseImageFromGallery()
				1 -> takePhotoFromCamera()
			}
		}
		pictureDialog?.show()
	}

	fun chooseImageFromGallery() {
		val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
		startActivityForResult(galleryIntent, GALLERY)
	}

	companion object {
		private const val IMAGE_DIRECTORY = "/pwd-senior-booklet"
	}

	private fun takePhotoFromCamera() {
		Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
			// Ensure that there's a camera activity to handle the intent
			try{

				val photoDirectory = File (
					context?.getExternalFilesDir(null).toString() + IMAGE_DIRECTORY)

				if (!photoDirectory.exists())
				{
					photoDirectory.mkdirs()
				}

				val fle = File(photoDirectory,"tempImage.jpg")
				fle.createNewFile()
				val tempUri = context?.let {
					FileProvider.getUriForFile(
						it,
						"com.seniorcitizen.app.fileprovider",
						fle
					)
				}

				captureImgFile = fle

				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri)

			}catch(ex : IOException){
				ex.printStackTrace()
			}

			context?.packageManager?.let {
				takePictureIntent.resolveActivity(it)?.also {
					startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
				}
			}
		}
	}

	fun getReadableFileSize(size: Long): String {
		if (size <= 0) {
			return "0"
		}
		val units = arrayOf("B", "KB", "MB", "GB", "TB")
		val digitGroups = (Math.log10(size.toDouble()) / Math.log10(1024.0)).toInt()
		return DecimalFormat("#,##0.#").format(
			size / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
	}

	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)
		if (requestCode == GALLERY)
		{
			if (data == null) {
				Timber.i("Failed to open picture!")
				return
			}

			try{
				actualImage = context?.let { FileUtil.from(it, data.data!!) } //saves in cache
				Timber.i("path: %s",actualImage!!.absolutePath)
				Timber.i("size: %s",actualImage!!.length() / 1024)
				Timber.i("size: %s",getReadableFileSize(actualImage!!.length()))
				//check the file size not greater than 500kb else do a compression
				val actualImgSize = actualImage!!.length() / 1024 // in KB
				if (actualImgSize > 500){
					//do compression
					val com = Compressor(context)

					disposable = com
						.compressToFileAsFlowable(actualImage)
						.subscribeOn(Schedulers.io())
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe({
							compressedImage = it
							Timber.i("compressedImage size: %s",compressedImage!!.length() / 1024)
							Timber.i("compressedImage size: %s",getReadableFileSize(compressedImage!!.length()))
							Timber.i("compressedImage path: %s",compressedImage!!.absolutePath)

							saveImage(BitmapFactory.decodeFile(compressedImage!!.absolutePath))
							Timber.i("Image Saved: $currentPhotoPath")
							Glide.with(this).load(currentPhotoPath).into(profile_img)

						},{
							it.printStackTrace()
							Timber.i(it.message)
						})

				}else{
					//save image and set pic
					val passedImg = BitmapFactory.decodeFile(actualImage!!.absolutePath)
					saveImage(passedImg)
					setPic()
				}


			}catch (e: IOException){
				Timber.i("Failed to read picture data!")
				e.printStackTrace()
			}
		}
		else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == DaggerAppCompatActivity.RESULT_OK)
		{

			if (captureImgFile!=null){

				try{
					// val capImage = FileUtil.from(this, data?.data!!)
					var compressingFile: File?

					Timber.i("path: %s", captureImgFile!!.absolutePath)
					Timber.i("size: %s",captureImgFile!!.length() / 1024)
					Timber.i("size: %s",getReadableFileSize(captureImgFile!!.length()))
					//check the file size not greater than 500kb else do a compression
					val actualImgSize = captureImgFile!!.length() / 1024 // in KB
					if (actualImgSize > 500){
						//do compression
						val com = Compressor(context)

						disposable = com
							.compressToFileAsFlowable(captureImgFile)
							.subscribeOn(Schedulers.io())
							.observeOn(AndroidSchedulers.mainThread())
							.subscribe({
								compressingFile = it
								Timber.i("compressedImage size: %s",compressingFile!!.length() / 1024)
								Timber.i("compressedImage size: %s",getReadableFileSize(compressingFile!!.length()))
								Timber.i("compressedImage path: %s",compressingFile!!.absolutePath)

								saveImage(BitmapFactory.decodeFile(compressingFile!!.absolutePath))

								//delete temp file
								val s0 = captureImgFile!!.delete()
								Timber.i("delete temp file: $s0")

								Timber.i("Image Saved: $currentPhotoPath")
								Glide.with(this).load(currentPhotoPath).into(profile_img)

							},{
								it.printStackTrace()
								Timber.i(it.message)
							})

					}else{
						//save image and set pic
						val passedImg = BitmapFactory.decodeFile(captureImgFile!!.absolutePath)
						saveImage(passedImg)

						//delete temp file
						val s0 = captureImgFile!!.delete()
						Timber.i("delete temp file: $s0")

						setPic()
					}

				}catch (e: IOException){
					Timber.i("Failed to read picture data!")
					e.printStackTrace()
				}
			}
		}
	}

	private fun setPic() {
		Timber.i("Image Saved: $currentPhotoPath")
		Glide.with(this).load(currentPhotoPath).into(profile_img)
		// profile_img.setBackgroundColor(getRandomColor())
	}

	fun saveImage(myBitmap: Bitmap) {
		val bytes = ByteArrayOutputStream()
		myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

		val b = bytes.toByteArray()
		val stringb = Base64.encodeToString(b,Base64.DEFAULT)
		Timber.i("encodeToString: $stringb")
		selectedImageString = stringb

		val photoDirectory = File (
			context?.getExternalFilesDir(null).toString() + IMAGE_DIRECTORY)

		if (!photoDirectory.exists())
		{
			photoDirectory.mkdirs()
		}
		try
		{
			Timber.i("photoDirectory: %s", photoDirectory.toString())

			val f = File(photoDirectory, ((Calendar.getInstance()
				.getTimeInMillis()).toString() + ".jpg"))
			f.createNewFile()
			val fo = FileOutputStream(f)
			fo.write(bytes.toByteArray())
			MediaScannerConnection.scanFile(context, arrayOf(f.getPath()), arrayOf("image/jpg"), null)
			fo.close()

			Timber.i("getAbsolutePath: %s" + f.getAbsolutePath())
			Timber.i("getAbsolutePath: %s" + f.length())
			currentPhotoPath = f.absolutePath
		}
		catch (e1: IOException){
			e1.printStackTrace()
		}
	}

	private fun handleBirthdayDatePick() {

		et_birthday.hint = "MM-dd-yyyy"

		val cal = Calendar.getInstance()

		val dateSetListener =
			DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
				cal.set(Calendar.YEAR, year)
				cal.set(Calendar.MONTH, monthOfYear)
				cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

				val myFormat = "yyyy-MM-dd'T'HH:mm:ss"
				val sdf = SimpleDateFormat(myFormat, Locale.US)
				val selectedDate = sdf.format(cal.time)
				selectedBirthday = selectedDate

				val form = formatBirthday(selectedDate)
				et_birthday.setText(form)
			}

		et_birthday.setOnClickListener {
			val dpd = context?.let { it1 ->
				DatePickerDialog(
					it1, dateSetListener,
					cal.get(Calendar.YEAR),
					cal.get(Calendar.MONTH),
					cal.get(Calendar.DAY_OF_MONTH)
				)
			}

			dpd?.datePicker?.maxDate = System.currentTimeMillis()
			dpd?.show()
		}
	}

	fun getAge(
		stringDate: String?
	): Int? {

		val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US)
		val date = parser.parse(stringDate!!)

		val year: Int? = date?.year
		val month: Int? = date?.month
		val day: Int? = date?.day

		//calculating age from dob
		val dob: Calendar = Calendar.getInstance()
		val today: Calendar = Calendar.getInstance()
		dob.set(year!!, month!!, day!!)
		// var age: Int = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR)
		// if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
		// 	age--
		// }
		// Timber.i("%s/year-%s,month-%s,day-%s",date,year,month,day)
		// Timber.i("%s",age)

		val projectedAge = calculateAgeWithJava7(date,today.time)

		return projectedAge
	}

	fun calculateAgeWithJava7(
		birthDate: Date?,
		currentDate: Date?
	): Int { // validate inputs ...
		val formatter: DateFormat = SimpleDateFormat("yyyyMMdd")
		val d1: Int = formatter.format(birthDate).toInt()
		val d2: Int = formatter.format(currentDate).toInt()
		return (d2 - d1) / 10000
	}

	override fun onStart() {
		super.onStart()
		Timber.i("onStart")
	}

	override fun onResume() {
		super.onResume()
		Timber.i("onResume , ${homeActivityViewModel?.seniorCitizenResult?.value}")
	}

	private fun disableEditing(){
		et_first_name.isEnabled = false
		et_middle_name.isEnabled = false
		et_last_name.isEnabled = false
		et_birthday.isEnabled = false
		et_address.isEnabled = false
		et_user_name.isEnabled = false
		et_user_password.isEnabled = false

		radio_pwd.isEnabled = false
		radio_senior.isEnabled = false
		radio_male.isEnabled = false
		radio_female.isEnabled = false
	}

	private fun enableEditing(){
		et_first_name.isEnabled = true
		et_middle_name.isEnabled = true
		et_last_name.isEnabled = true
		et_birthday.isEnabled = true
		et_address.isEnabled = true
		et_user_name.isEnabled = true
		et_user_password.isEnabled = true

		radio_pwd.isEnabled = true
		radio_senior.isEnabled = true
		radio_male.isEnabled = true
		radio_female.isEnabled = true
	}

	private fun covertImage(encodedImage: String): Bitmap{

		val decodedString: ByteArray = Base64.decode(encodedImage, Base64.DEFAULT)
		return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
	}
}
