package com.seniorcitizen.app.ui.register

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.media.MediaScannerConnection
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.view.View
import android.widget.RadioButton
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.seniorcitizen.app.R
import com.seniorcitizen.app.data.model.RegisterRequest
import com.seniorcitizen.app.databinding.ActivityRegisterBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.ui.login.LoginActivity
import com.seniorcitizen.app.utils.FileUtil
import com.wajahatkarim3.easyvalidation.core.view_ktx.validator
import id.zelory.compressor.Compressor
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.longToast
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

class RegisterActivity: BaseActivity<ActivityRegisterBinding>(), RegisterCallback {

    private var disposable : Disposable? = null

    private val viewModel: RegisterViewModel by viewModels {
        viewModelFactory
    }

    private var actualImage: File? = null
    private var compressedImage: File? = null
    private var captureImgFile: File? = null
    private var selectedImageString : String? = null

    private var isFirstNameValid = false
    private var isMiddleNameValid = false
    private var isLastNameValid = false
    private var isAddressValid = false
    private var isUserNameValid = false
    private var isUserPasswordValid = false

    override fun getContentView(): Int = R.layout.activity_register

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        viewModel.apply { init(this@RegisterActivity) }

        getBinding()?.let {
            it.viewModel = viewModel
            it.user = RegisterRequest()
        }

        observersRegister()

        iv_back.setOnClickListener {
            // pop to last stack
            finish()
        }

        handleBirthdayDatePick()

        profile_img!!.setOnClickListener{ showPictureDialog() }

        registrationfieldsValidation()

        btn_register.setOnClickListener {

            // null check
            if(!et_user_name.text.isNullOrBlank() || !et_password.text.isNullOrBlank()
                || !et_first_name.text.isNullOrBlank()
                || !et_middle_name.text.isNullOrBlank()
                || !et_last_name.text.isNullOrBlank()
                || !et_address.text.isNullOrBlank())
            {
                // validitity check
                if(isUserNameValid
                    && isUserPasswordValid
                    && isFirstNameValid
                    && isMiddleNameValid
                    && isLastNameValid
                    && isAddressValid)
                {
                    if (selectedImageString!=null){

                        viewModel.doRegister(getBinding()?.user,selectedImageString)

                    }else{

                        toast("Please Select a profile image.")
                    }
                }else{
                    toast("Please correct the validation.")
                }
            }else{
                toast("Please complete the details.")
            }
        }


        tv_to_login.setOnClickListener { toLoginPage() }
    }

    private fun registrationfieldsValidation() {

        val userFirstNameWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                isFirstNameValid = et_first_name.validator()
                    .nonEmpty()
                    .minLength(2)
                    .addErrorCallback {
                        et_first_name.error = it
                    }
                    .addSuccessCallback {

                        et_first_name.error = null
                    }
                    .check()
            }
        }

        val userMiddleNameWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                isMiddleNameValid = et_middle_name.validator()
                    .nonEmpty()
                    .minLength(2)
                    .addErrorCallback {
                        et_middle_name.error = it
                    }
                    .addSuccessCallback {

                        et_middle_name.error = null
                    }
                    .check()
            }
        }

        val userLastNameWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                isLastNameValid = et_last_name.validator()
                    .nonEmpty()
                    .minLength(2)
                    .addErrorCallback {
                        et_last_name.error = it
                    }
                    .addSuccessCallback {

                        et_last_name.error = null
                    }
                    .check()
            }
        }

        val userAddressWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                isAddressValid = et_address.validator()
                    .nonEmpty()
                    .minLength(2)
                    .addErrorCallback {
                        et_address.error = it
                    }
                    .addSuccessCallback {

                        et_address.error = null
                    }
                    .check()
            }
        }

        val userNameWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                isUserNameValid = et_user_name.validator()
                    .nonEmpty()
                    .minLength(2)
                    .addErrorCallback {
                        et_user_name.error = it
                    }
                    .addSuccessCallback {

                        et_user_name.error = null
                    }
                    .check()
            }
        }

        val passwordWatcher = object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                isUserPasswordValid = et_password.validator()
                    .nonEmpty()
                    .minLength(8)
                    .atleastOneUpperCase()
                    .atleastOneLowerCase()
                    .atleastOneNumber()
                    .atleastOneSpecialCharacters()
                    .addErrorCallback {

                        ti_password.error = it
                        ti_password.errorIconDrawable = null
                    }
                    .addSuccessCallback {

                        ti_password.error = null
                    }
                    .check()
            }
        }

        et_first_name.addTextChangedListener(userFirstNameWatcher)
        et_middle_name.addTextChangedListener(userMiddleNameWatcher)
        et_last_name.addTextChangedListener(userLastNameWatcher)
        et_address.addTextChangedListener(userAddressWatcher)
        et_user_name.addTextChangedListener(userNameWatcher)
        et_password.addTextChangedListener(passwordWatcher)
    }

    private val GALLERY = 1
    private val REQUEST_IMAGE_CAPTURE = 2
    lateinit var currentPhotoPath: String

    private fun showPictureDialog() {
        val pictureDialog = AlertDialog.Builder(this)
        pictureDialog.setTitle("Select Action")
        val pictureDialogItems = arrayOf("Select image from gallery", "Capture photo from camera")
        pictureDialog.setItems(pictureDialogItems
        ) { dialog, which ->
            when (which) {
                0 -> chooseImageFromGallery()
                1 -> takePhotoFromCamera()
            }
        }
        pictureDialog.show()
    }

    fun chooseImageFromGallery() {
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, GALLERY)
    }

    private fun takePhotoFromCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            try{

                val photoDirectory = File (
                    this.getExternalFilesDir(null).toString() + IMAGE_DIRECTORY)

                if (!photoDirectory.exists())
                {
                    photoDirectory.mkdirs()
                }

                val fle = File(photoDirectory,"tempImage.jpg")
                fle.createNewFile()
                val tempUri = FileProvider.getUriForFile(
                    this,
                    "com.seniorcitizen.app.fileprovider",
                    fle
                )

                captureImgFile = fle

                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri)

            }catch(ex : IOException){
                ex.printStackTrace()
            }

            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY)
        {
            if (data == null) {
                showError("Failed to open picture!")
                return
            }

            try{
                actualImage = FileUtil.from(this, data.data!!) //saves in cache
                Timber.i("path: %s",actualImage!!.absolutePath)
                Timber.i("size: %s",actualImage!!.length() / 1024)
                Timber.i("size: %s",getReadableFileSize(actualImage!!.length()))
                //check the file size not greater than 500kb else do a compression
                val actualImgSize = actualImage!!.length() / 1024 // in KB
                if (actualImgSize > 500){
                    //do compression
                    val com = Compressor(this)

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
                        longToast("Image Saved: $currentPhotoPath")
                        Glide.with(this).load(currentPhotoPath).into(profile_img)

                    },{
                        it.printStackTrace()
                        showError(it.message)
                    })

                }else{
                    //save image and set pic
                    val passedImg = BitmapFactory.decodeFile(actualImage!!.absolutePath)
                    saveImage(passedImg)
                    setPic()
                }


            }catch (e: IOException){
                showError("Failed to read picture data!")
                e.printStackTrace()
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
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
                        val com = Compressor(this)

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

                                longToast("Image Saved: $currentPhotoPath")
                                Glide.with(this).load(currentPhotoPath).into(profile_img)

                            },{
                                it.printStackTrace()
                                showError(it.message)
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
                    showError("Failed to read picture data!")
                    e.printStackTrace()
                }
            }
        }
    }

    fun saveImage(myBitmap: Bitmap) {
        val bytes = ByteArrayOutputStream()
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes)

        val b = bytes.toByteArray()
        val stringb = Base64.encodeToString(b,Base64.DEFAULT)
        Timber.i("encodeToString: $stringb")
        selectedImageString = stringb

        val photoDirectory = File (
            this.getExternalFilesDir(null).toString() + IMAGE_DIRECTORY)

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
            MediaScannerConnection.scanFile(this, arrayOf(f.getPath()), arrayOf("image/jpg"), null)
            fo.close()

            Timber.i("getAbsolutePath: %s" + f.getAbsolutePath())
            Timber.i("getAbsolutePath: %s" + f.length())
            currentPhotoPath = f.absolutePath
        }
        catch (e1: IOException){
            e1.printStackTrace()
        }
    }

    private fun setPic() {
        longToast("Image Saved: $currentPhotoPath")
        Glide.with(this).load(currentPhotoPath).into(profile_img)
        profile_img.setBackgroundColor(getRandomColor())
    }

    companion object {
        private const val IMAGE_DIRECTORY = "/pwd-senior-booklet"
    }

    private fun observersRegister() {
        viewModel.getRegisterLiveData().observe(this, Observer { response ->
            if (response != null) {
                Timber.i("$response")
            }
        })

        viewModel.onProgressBar().observe(this, Observer { loading ->
            if (loading) {
                btn_register.isEnabled = false
                progress_bar.visibility = View.VISIBLE
            } else {
                btn_register.isEnabled = true
                progress_bar.visibility = View.GONE
            }
        })

        viewModel.getRegisterLiveError().observe(this, Observer { error ->
            if (error != null) {
                Timber.e(error)
            }
        })
    }

    private fun handleBirthdayDatePick() {

        // et_birthday.hint = "yyyy-MM-dd"

        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "yyyy-MM-dd" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                val selectedDate = sdf.format(cal.time)
                et_birthday.setText(selectedDate)
            }

        et_birthday.setOnClickListener {
            val dpd = DatePickerDialog(
                this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            )

            dpd.datePicker.maxDate = System.currentTimeMillis()
            dpd.show()
        }
    }

    fun onRadioButtonStatusClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_senior ->
                    if (checked) {
                        getBinding()?.user?.IsSenior = true
                        getBinding()?.user?.IsPWD = false
                    }
                R.id.radio_pwd ->
                    if (checked) {
                        getBinding()?.user?.IsSenior = false
                        getBinding()?.user?.IsPWD = true
                    }
            }
        }
    }

    fun onRadioButtonSexClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_male ->
                    if (checked) {
                        getBinding()?.user?.Sex = "Male"
                    }
                R.id.radio_female ->
                    if (checked) {
                        getBinding()?.user?.Sex = "Female"
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

    private fun getRandomColor(): Int {
        val rand = Random()
        return Color.argb(100,
            rand.nextInt(256),
            rand.nextInt(256),
            rand.nextInt(256))
    }

    fun showError(errorMessage: String?) {
        if (errorMessage != null) {
            toast(errorMessage)
        }
    }


    override fun onSuccess() {
        onToastMessage(getString(R.string.registration_success))
        toLoginPage()
    }

    override fun onFailure(responseMessage: String, responseCode: Int) {
        if (responseCode == 406)
            onToastMessage(getString(R.string.error_registration_account_with_same_email_already_exist))
        else
            onToastMessage(responseMessage)
    }

    private fun toLoginPage() {
        startActivity<LoginActivity>()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disposeElements()
        if(disposable!=null && !disposable!!.isDisposed){
            disposable!!.dispose()
        }
    }
}