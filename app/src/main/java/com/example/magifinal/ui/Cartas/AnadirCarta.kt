package com.example.magifinal.ui.Cartas

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.magifinal.HomeAdmin
import com.example.magifinal.R
import com.example.magifinal.Utilidades
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class AnadirCarta : AppCompatActivity(), CoroutineScope {


    private lateinit var nombre: EditText
    private lateinit var categoria: Spinner
    private lateinit var precio: EditText
    private lateinit var stock: EditText
    private lateinit var imagen: ImageView

    private lateinit var crear: Button
    private lateinit var volver: Button

    private lateinit var job: Job

    private var categoriaSeleccionada: String = ""


        private var url_carta: Uri? = null

    private lateinit var db_ref: DatabaseReference
    private lateinit var sto_ref: StorageReference

    private lateinit var lista_cartas: MutableList<Carta>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_carta)


        job = Job()
        val thisActivity = this

        nombre = findViewById(R.id.nombreCarta)
        categoria = findViewById(R.id.categoriaCarta)
        precio = findViewById(R.id.precioCarta)
        stock = findViewById(R.id.stockCarta)
        imagen = findViewById(R.id.imagenCarta)
        crear = findViewById(R.id.botonAnadirCarta)
        volver = findViewById(R.id.botonVolver)


        db_ref = FirebaseDatabase.getInstance().reference
        sto_ref = FirebaseStorage.getInstance().reference

        lista_cartas = mutableListOf()

        ArrayAdapter.createFromResource(
            this,
            R.array.categorias,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categoria.adapter = adapter
        }


        categoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                categoriaSeleccionada = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        crear.setOnClickListener {
            if (nombre.toString().trim().isEmpty() || precio.toString().trim()
                    .isEmpty() || stock.toString().trim().isEmpty() || url_carta.toString().trim()
                    .isEmpty() || categoria.toString().trim().isEmpty()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Por favor, rellene todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (Utilidades.existecarta(lista_cartas, nombre.text.toString().trim())) {

                Toast.makeText(
                    applicationContext,
                    "Ya existe una carta con ese nombre",
                    Toast.LENGTH_SHORT
                ).show()

            } else {
                var id_generada = db_ref.child("Tienda").child("Cartas").push().key


                launch {
                    val url_foto =
                        Utilidades.guardarImagenCarta(sto_ref, id_generada!!, url_carta!!)

                    var carta =
                        Carta(
                            id_generada,
                            nombre.text.toString().trim(),
                            categoriaSeleccionada,
                            precio.text.toString().trim(),
                            stock.text.toString().trim(),
                            url_foto
                        )

                    Utilidades.crearCarta(db_ref, carta)

                    Toast.makeText(
                        applicationContext,
                        "Carta añadida correctamente",
                        Toast.LENGTH_SHORT
                    ).show()

                    Utilidades.toastCorrutina(
                        thisActivity,
                        applicationContext,
                        "Carta añadida correctamente"
                    )

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
            url_carta = uri
            imagen.setImageURI(uri)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

}