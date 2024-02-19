package com.example.magifinal

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.example.magifinal.ui.Cartas.Carta
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

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

        suspend fun guardarCarta(sto_ref: StorageReference, id:String, imagen: Uri):String{
            lateinit var url_carta_firebase: Uri
            sto_ref.child("Cartas").child(id).putFile(imagen).await()
            url_carta_firebase = sto_ref.child("Cartas").child("Fotos").child(id).putFile(imagen).await().storage.downloadUrl.await()

            return url_carta_firebase.toString()
        }

        fun existecarta(cartas : List<Carta>, nombre: String): Boolean{
            return cartas.any { it.nombre!!.lowercase() == nombre.lowercase() }
        }

        fun toastCorrutina(activity: AppCompatActivity, contexto: Context, texto: String){
            activity.runOnUiThread{
                Toast.makeText(contexto, texto, Toast.LENGTH_SHORT).show()
            }
        }

        fun animacion_carga(contexto: Context): CircularProgressDrawable {
            val animacion = CircularProgressDrawable(contexto)
            animacion.strokeWidth = 5f
            animacion.centerRadius = 30f
            animacion.start()
            return animacion
        }

        val transicion = DrawableTransitionOptions.withCrossFade(500)

        fun opcionesGlide(context: Context): RequestOptions {
            val options = RequestOptions()
                .placeholder(animacion_carga(context))
                .fallback(R.drawable.magic)
                .error(R.drawable.error)
            return options
        }

        suspend fun guardarImagenCarta(sto_ref: StorageReference, id: String, imagen: Uri): String {
            lateinit var url_carta_firebase: Uri
            url_carta_firebase = sto_ref.child("Cartas").child("Fotos").child(id).putFile(imagen).await().storage.downloadUrl.await()
            return url_carta_firebase.toString()
        }



    }

}