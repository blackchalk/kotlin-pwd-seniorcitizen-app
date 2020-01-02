package com.seniorcitizen.app.ui.home

import android.Manifest
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.seniorcitizen.app.R
import com.seniorcitizen.app.databinding.ActivityHomeBinding
import com.seniorcitizen.app.ui.base.BaseActivity
import com.seniorcitizen.app.utils.Constants
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.disposables.Disposable
import timber.log.Timber

/**
 * Created by Nic Evans on 2019-12-20.
 */
class HomeActivity: BaseActivity<ActivityHomeBinding>(), HomeCallback {

	private lateinit var appBarConfiguration : AppBarConfiguration
	private lateinit var disposable : Disposable

	private val viewModel: HomeActivityViewModel by viewModels {
		viewModelFactory
	}

	override fun getContentView(): Int = R.layout.activity_home

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val toolbar = findViewById<Toolbar>(R.id.toolbar)
		setSupportActionBar(toolbar)

		checkPermission()
	}

	override fun onActivityCreated(savedInstanceState: Bundle?) {

		val extra = intent.getStringExtra(Constants.INTENT_KEY) ?: null
		viewModel.apply { init(this@HomeActivity) }

		if (extra != null) {
			viewModel.setUserIdNumber(extra)
		}

		getBinding()?.let {
			it.viewModel = viewModel
		}

		viewModel.doRequetUser()

	}

	// asks user for permissions for camera and read/write storage
	private fun checkPermission() {
		val rxPermissions = RxPermissions(this) // where this is an Activity or Fragment instance

		// Must be done during an initialization phase like onCreate
		disposable = rxPermissions
			.request(Manifest.permission.WRITE_EXTERNAL_STORAGE
				, Manifest.permission.READ_EXTERNAL_STORAGE
				,Manifest.permission.CAMERA)
			.subscribe { granted: Boolean ->
				if (granted) { // Always true pre-M
					init()
				} else { // Oups permission denied
					Timber.e("Permission denied")
					finish()
				}
			}
	}

	// prepare the bottom navigation and navigation controllers
	private fun init() {

		val host: NavHostFragment = supportFragmentManager
			.findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

		// Set up Action Bar
		val navController = host.navController

		appBarConfiguration = AppBarConfiguration(navController.graph)

		setupActionBar(navController, appBarConfiguration)

		setupBottomNavMenu(navController)

		navController.addOnDestinationChangedListener { _, destination, _ ->
			val dest: String = try {
				resources.getResourceName(destination.id)
			} catch (e: Resources.NotFoundException) {
				Integer.toString(destination.id)
			}

			Timber.i( "Navigated to $dest")
		}
	}

	private fun setupActionBar(navController: NavController,
		appBarConfig : AppBarConfiguration) {
		setupActionBarWithNavController(navController, appBarConfig)
	}

	private fun setupBottomNavMenu(navController: NavController) {
		val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav_view)
		bottomNav?.setupWithNavController(navController)
	}
	   override fun onSupportNavigateUp(): Boolean {
       // Allows NavigationUI to support proper up navigation or the drawer layout
       // drawer menu, depending on the situation
       return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
   }

	override fun onDestroy() {
		super.onDestroy()
		if (!disposable.isDisposed){
			disposable.dispose()
		}
		viewModel.disposeElements()
	}

	override fun onSuccess() {

	}
}
