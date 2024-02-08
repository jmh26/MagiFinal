package com.example.magifinal

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Utilidades {

    companion object{

        fun crearUsuario(id:String, email: String, contra:String, rol: String){
            var db_ref = FirebaseDatabase.getInstance().reference
            val usuario = Usuario(id,email, contra, rol)
            db_ref.child("Tienda").child("Usuarios").child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(usuario)
        }

        fun esAdmin(email: String, contra: String): Boolean {
            return email == "administrador@gmail.com" && contra == "administrador"
        }

        fun obtenerRol(email:String, contra: String, auth: FirebaseAuth): String{
            return if (email == "administrador@gmail.com" && contra == "administrador") {
                "administrador"
            }else{
                "usuario"
            }
        }
        fun crearCarta(dtb_ref: DatabaseReference, carta: Carta){
            dtb_ref.child("Tienda").child("Cartas").child(carta.id!!).setValue(carta)
        }

        fun existecarta(cartas : List<Carta>, nombre: String): Boolean{
            return cartas.any { it.nombre!!.lowercase() == nombre.lowercase() }
        }

    }

}