package com.raquezha.heograpiya

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.raquezha.heograpiya.databinding.ActivityMainBinding
import androidx.navigation.fragment.NavHostFragment
import com.raquezha.heograpiya.shared.contentView

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by contentView(R.layout.activity_main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {

            val navHostFragment = supportFragmentManager.findFragmentById(
                R.id.nav_host
            ) as NavHostFragment

            val navController = navHostFragment.navController

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            val appBarConfiguration = AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_dashboard,
                    R.id.navigation_notifications
                )
            )
            setupActionBarWithNavController(
                navController,
                appBarConfiguration
            )
            bottomNav.setupWithNavController(navController)
        }
    }
}