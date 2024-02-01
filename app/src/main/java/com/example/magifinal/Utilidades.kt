package com.example.magifinal

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Utilidades {

    companion object{

        fun crearUsuario(id:String, email: String, contra:String){
            var db_ref = FirebaseDatabase.getInstance().reference
            val usuario = Usuario(id,email, contra)
            db_ref.child("Tienda").child("Usuarios").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(usuario)
        }



    }

}