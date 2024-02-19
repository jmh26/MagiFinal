package com.example.magifinal

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate

class Ajustes: AppCompatActivity() {


    private lateinit var lightThemeButton: ImageView
    private lateinit var darkThemeButton: ImageView
    private lateinit var volver: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences = getSharedPreferences("ThemePref", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("DarkTheme", false)) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.LightTheme)
        }

        setContentView(R.layout.activity_ajustes)

        lightThemeButton = findViewById(R.id.light_theme_button)
        darkThemeButton = findViewById(R.id.dark_theme_button)

        lightThemeButton.setOnClickListener {
            sharedPreferences.edit().putBoolean("DarkTheme", false).apply()
            recreate()
        }

        darkThemeButton.setOnClickListener {
            sharedPreferences.edit().putBoolean("DarkTheme", true).apply()
            recreate()
        }

        volver = findViewById(R.id.btnVolver)
        volver.setOnClickListener {
            var intent = Intent(this, HomeAdmin::class.java)
            startActivity(intent)
        }
    }
}