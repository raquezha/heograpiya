package com.raquezha.heograpiya

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.raquezha.heograpiya.databinding.ActivityMainBinding
import androidx.navigation.fragment.NavHostFragment


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//
//        binding = ActivityMainBinding.inflate(layoutInflater)
//
//        setContentView(binding.root)
//
//        val navView: BottomNavigationView = binding.navView
//
//        val navHostFragment = supportFragmentManager.findFragmentById(
//            R.id.nav_controller_view_tag
//        ) as NavHostFragment
//        val navController = navHostFragment.navController
//
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.navigation_home,
//                R.id.navigation_dashboard,
//                R.id.navigation_notifications
//            )
//        )
//        setupActionBarWithNavController(
//            navController,
//            appBarConfiguration
//        )
//        navView.setupWithNavController(navController)
    }
}