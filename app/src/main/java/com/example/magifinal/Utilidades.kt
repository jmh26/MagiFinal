package com.example.magifinal

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Utilidades {

    companion object{

        fun crearUsuario(id:String, email: String, contra:String, rol: String){
            var db_ref = FirebaseDatabase.getInstance().reference
            val usuario = Usuario(id,email, contra, rol)
            db_ref.child("Tienda").child("Usuarios").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(usuario)
        }



        fun obtenerRol(email:String, contra: String, auth: FirebaseAuth): String{
            return if (email == "admin" && contra == "admin") {
                "admin"
            }else{
                "usuario"
            }
        }
    }

}