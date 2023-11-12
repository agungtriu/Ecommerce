package com.agungtriu.ecommerce.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.ActivityMainBinding
import com.agungtriu.ecommerce.helper.Utils.setLanguage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavController()
        observeData()
        checkNotificationPermission()
    }

    private fun setupNavController() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
    }


    private fun observeData() {
        if (!viewModel.getLoginStatus()) {
            navController.navigate(R.id.action_global_to_prelogin_navigation)
        }

        viewModel.getAuthorizedStatus().distinctUntilChanged().observe(this) {
            if (!it.isAuthorized) {
                navController.navigate(R.id.action_global_to_prelogin_navigation)
            }
        }

        viewModel.getThemeLang().distinctUntilChanged().observe(this) {
            AppCompatDelegate.setDefaultNightMode(if (it.isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            setLanguage(it.language)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
            if (!isPermissionsGranted(notificationPermission))
                requestPermissionLauncher.launch(notificationPermission)
        }
    }

    private fun isPermissionsGranted(permission: String) =
        ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (!isGranted)
                finish()
        }


    fun toMain() {
        navController.navigate(R.id.action_global_to_main_navigation)
    }

    fun toDetail(bundle: Bundle) {
        navController.navigate(R.id.action_global_to_detail_fragment, bundle)
    }
}