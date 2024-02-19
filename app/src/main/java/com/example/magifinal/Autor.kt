package com.example.magifinal

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Autor: AppCompatActivity() {
    private lateinit var volver: Button
    override fun onCreate(savedInstanceState: Bundle?) {

        val sharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("DarkTheme", false)) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_autor)

        volver = findViewById(R.id.btnVolver)
        volver.setOnClickListener {
            var intent = Intent(this, HomeAdmin::class.java)
            startActivity(intent)
        }


    }
}