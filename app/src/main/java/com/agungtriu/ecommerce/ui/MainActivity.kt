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
import androidx.core.os.LocaleListCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.distinctUntilChanged
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.agungtriu.ecommerce.R
import com.agungtriu.ecommerce.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
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
        viewModel.getAuthorizedStatus().distinctUntilChanged().observe(this) {
            if (!it.isAuthorized) {
                navController.navigate(R.id.action_global_to_prelogin_navigation)
            }
        }
        viewModel.getThemeLang().distinctUntilChanged().observe(this) {
            AppCompatDelegate.setDefaultNightMode(
                if (it.isDark) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )
            val languageIn: LocaleListCompat = LocaleListCompat.forLanguageTags(it.language)
            AppCompatDelegate.setApplicationLocales(languageIn)
        }
    }

    private fun checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val notificationPermission = Manifest.permission.POST_NOTIFICATIONS
            if (!isPermissionsGranted(notificationPermission)) {
                requestPermissionLauncher.launch(notificationPermission)
            }
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
            if (isGranted) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.main_permission_request_accepted), Snackbar.LENGTH_SHORT
                ).show()
            } else {
                Snackbar.make(
                    binding.root,
                    getString(R.string.main_permission_request_denied), Snackbar.LENGTH_SHORT
                ).show()

            }
        }

    fun navigate(action: Int, bundle: Bundle? = null) {
        navController.navigate(action, bundle)
    }
}
