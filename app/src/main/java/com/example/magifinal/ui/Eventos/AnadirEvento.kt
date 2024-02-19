package com.example.magifinal.ui.Eventos

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.magifinal.HomeAdmin
import com.example.magifinal.R
import com.example.magifinal.Utilidades
import com.example.magifinal.ui.Cartas.Carta
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AnadirEvento : AppCompatActivity(), CoroutineScope {

    private lateinit var nombre: EditText
    private lateinit var fecha: EditText
    private lateinit var precio: EditText
    private lateinit var aforomax: EditText
    private lateinit var imagen: ImageView


    private lateinit var crear: Button
    private lateinit var volver: Button

    private lateinit var job: Job

    private var url_evento: Uri? = null

    private lateinit var db_ref: DatabaseReference
    private lateinit var sto_ref: StorageReference


    private lateinit var lista_eventos: MutableList<Evento>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_evento)

        job = Job()
        val thisActivity = this

        nombre = findViewById(R.id.nombreEvento)
        fecha = findViewById(R.id.fechaEvento)
        precio = findViewById(R.id.precioEvento)
        aforomax = findViewById(R.id.aforoMaximo)

        imagen = findViewById(R.id.imagenEvento)
        crear = findViewById(R.id.botonAnadirEvento)
        volver = findViewById(R.id.botonVolverEvento)

        db_ref = FirebaseDatabase.getInstance().reference
        sto_ref = FirebaseStorage.getInstance().reference

        lista_eventos = mutableListOf()


        crear.setOnClickListener {
            if (nombre.toString().isEmpty() || fecha.toString().isEmpty() || precio.toString()
                    .isEmpty() || aforomax.toString().isEmpty()
            ) {
                Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show()

            } else if (Utilidades.existeevento(lista_eventos, nombre.text.toString().trim())) {
                Toast.makeText(this, "El evento ya existe", Toast.LENGTH_SHORT).show()


            } else {
                var id_generada = db_ref.child("Tienda").child("Eventos").push().key

                launch {
                    val url_foto =
                        Utilidades.guardarImagenEvento(sto_ref, id_generada!!, url_evento!!)

                    var evento = Evento(
                        id_generada,
                        nombre.text.toString(),
                        fecha.text.toString(),
                        precio.text.toString().trim(),
                        aforo_actual = "0",
                        estado = "no apuntado",
                        aforomax.text.toString().trim(),
                        url_foto
                    )

                    Utilidades.crearEvento(db_ref, evento)

                    Toast.makeText(thisActivity, "Evento añadido", Toast.LENGTH_SHORT).show()

                    Utilidades.toastCorrutina(thisActivity, applicationContext, "Evento añadido")

                    val activity = Intent(applicationContext, HomeAdmin::class.java)
                    startActivity(activity)

                }

            }
        }
        volver.setOnClickListener {
            val activity = Intent(applicationContext, HomeAdmin::class.java)
            startActivity(activity)
        }
        imagen.setOnClickListener {
            accesoGaleria.launch("image/*")
        }

    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private val accesoGaleria = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            url_evento = uri
            imagen.setImageURI(uri)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

}