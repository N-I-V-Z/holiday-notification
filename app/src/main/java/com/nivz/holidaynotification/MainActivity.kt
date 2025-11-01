package com.nivz.holidaynotification

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.nivz.holidaynotification.databinding.ActivityMainBinding
import com.nivz.holidaynotification.ui.ViewPager2Adapter
import com.nivz.holidaynotification.viewmodel.HolidayViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for Holiday Notification app
 * Entry point of the application with 3-tab navigation
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: HolidayViewModel by viewModels()

    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var adapter: ViewPager2Adapter

    // Request permissions for notifications and alarms
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, ready to show notifications
        } else {
            // Permission denied, handle gracefully
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup ViewPager2 and Tabs
        setupTabs()

        // Request notification permission (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        // Observe ViewModel
        observeViewModel()
    }

    private fun setupTabs() {
        viewPager = binding.viewPager
        tabLayout = binding.tabLayout

        // Create adapter
        adapter = ViewPager2Adapter(this)
        viewPager.adapter = adapter

        // Connect TabLayout with ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getTabTitle(position)
        }.attach()
    }

    private fun observeViewModel() {
        // Observe error messages
        viewModel.errorMessage.observe(this) { errorMsg ->
            if (!errorMsg.isNullOrEmpty()) {
                showError(errorMsg)
                viewModel.clearErrorMessage()
            }
        }

        // Observe all holidays
        viewModel.allHolidays.observe(this) { holidays ->
            if (holidays != null && holidays.isNotEmpty()) {
                // Data loaded successfully
            }
        }
    }

    private fun showError(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }
}
