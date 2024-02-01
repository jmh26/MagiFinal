package com.example.magifinal

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Registro: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var user: FirebaseUser?=null
    private lateinit var etEmail: EditText
    private lateinit var etContra: EditText
    private lateinit var registro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        auth = FirebaseAuth.getInstance()


        etEmail = findViewById(R.id.etEmail)
        etContra = findViewById(R.id.etContrasena)
        registro = findViewById(R.id.btConfirmar)

        registro.setOnClickListener {
            val etEmail = etEmail.text.toString()
            val etContra = etContra.text.toString()

            if (etEmail.isNotEmpty() && etContra.isNotEmpty()) {
                registerUser(etEmail, etContra)
            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT)
                    .show()

            }

        }

    }




    private fun registerUser(id: String,email: String, contra: String) {
        auth.createUserWithEmailAndPassword(email, contra)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    user = auth.currentUser
                    Utilidades.crearUsuario(id,email, contra)
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                        Toast.makeText(this, "Usuario registrado correctamente", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this, "Error al crear el usuario", Toast.LENGTH_SHORT).show()
                }
            }


    }
}