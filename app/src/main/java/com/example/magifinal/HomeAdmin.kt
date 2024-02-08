package com.example.magifinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeAdmin: AppCompatActivity() {
private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)



        navController = findNavController(R.id.fragment_container)

        val bottomNavView = findViewById<BottomNavigationView>(R.id.navigation_bar)
        bottomNavView.setupWithNavController(navController)

        setupActionBarWithNavController(navController)
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentHomeAdmin()).commit()






    }
}