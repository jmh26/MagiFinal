package com.example.magifinal

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

private lateinit var auth: FirebaseAuth
private var user: FirebaseUser?=null
private lateinit var etEmail: EditText
private lateinit var etContra: EditText
private lateinit var registro: Button
private lateinit var noTengoCuenta: TextView

class Login: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        noTengoCuenta = findViewById(R.id.tvNoTengoCuenta)


        noTengoCuenta.setOnClickListener {
            startActivity(Intent(this, Registro::class.java))
        }

        etEmail = findViewById(R.id.etEmailL)
        etContra = findViewById(R.id.etContrasenaL)
        registro = findViewById(R.id.btConfirmarL)

        registro.setOnClickListener {
            val email = etEmail.text.toString()
            val contra = etContra.text.toString()

            if (email.isNotEmpty() && contra.isNotEmpty() && email == "admin" && contra == "admin") {


                loginUser(email, contra)

            } else if (email.isNotEmpty() && contra.isNotEmpty()) {

                loginUser(email, contra)

            } else {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT)
                    .show()
            }

        }


    }

    private fun loginUser(email: String, contra: String) {
        auth.signInWithEmailAndPassword(email, contra)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val currentUser: FirebaseUser? = auth.currentUser
                    val userId = currentUser?.uid
                    val rol = Utilidades.obtenerRol(email, contra, auth)
                    if(userId != null){

                        Utilidades.crearUsuario(userId,email, contra, rol)
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }



                } else {
                    Toast.makeText(this, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
                }
            }

        val sharedPref = getSharedPreferences("login", MODE_PRIVATE)
        val editor = sharedPref.edit()

        if (email.trim() == "admin" && contra.trim() == "admin") {
            editor.putString("rol", "admin")
        } else {
            editor.putString("rol", "usuario")
        }
        editor.apply()
    }


}

