package com.example.magifinal

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController

class HomeAdmin: AppCompatActivity() {
private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_admin)




        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, FragmentHomeAdmin()).commit()






    }
}