package com.example.magifinal

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
import com.bumptech.glide.Glide
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class EditarCarta: AppCompatActivity(),
    CoroutineScope {

    private lateinit var nombre: EditText
    private lateinit var categoria: Spinner
    private lateinit var precio: EditText
    private lateinit var stock: EditText
    private lateinit var imagen: ImageView

    private lateinit var editar: Button
    private lateinit var volver: Button

    private lateinit var job: Job

    private var url_carta: Uri? = null

    private lateinit var db_ref: DatabaseReference
    private lateinit var sto_ref: StorageReference

    private lateinit var lista_cartas: MutableList<Carta>
    private lateinit var pojo_carta: Carta


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_carta)

        job = Job()
        val thisActivity = this

        pojo_carta = intent.getParcelableExtra<Carta>("carta")!!

        nombre = findViewById(R.id.nombreCartaEdit)
        categoria = findViewById(R.id.categoriaCartaEdit)
        precio = findViewById(R.id.precioCartaEdit)
        stock = findViewById(R.id.stockCartaEdit)
        imagen = findViewById(R.id.imagenCartaEdit)
        volver = findViewById(R.id.botonVolverEdit)
        editar = findViewById(R.id.botonEditarCarta)
        nombre.setText(pojo_carta.nombre)
        precio.setText(pojo_carta.precio.toString())
        stock.setText(pojo_carta.stock.toString())

        Glide.with(applicationContext).load(pojo_carta.imagen).apply(Utilidades.opcionesGlide(applicationContext))
            .transition(Utilidades.transicion).into(imagen)


        db_ref = FirebaseDatabase.getInstance().getReference()
        sto_ref = FirebaseStorage.getInstance().getReference()


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
                val categoria = parent?.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        editar.setOnClickListener {
            if (nombre.text.toString().trim().isEmpty() ||
                precio.text.toString().trim().isEmpty() ||
                stock.text.toString().trim().isEmpty()  ||
                categoria.selectedItem.toString().trim().isEmpty() ||
                url_carta.toString().trim().isEmpty()
            ) {
                Toast.makeText(
                    applicationContext,
                    "Por favor, rellene todos los campos",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                var url_imagen_firebase = String()
                launch {
                    if (url_carta == null) {
                        url_imagen_firebase = pojo_carta.imagen!!
                    } else {
                        url_imagen_firebase = Utilidades.guardarCarta(
                            sto_ref,
                            pojo_carta.id!!,
                            url_carta!!


                        )
                    }
                    var carta = Carta(
                        nombre.text.toString(),
                        categoria.selectedItem.toString(),
                        precio.text.toString(),
                        stock.text.toString(),
                        url_imagen_firebase
                    )
                    Utilidades.crearCarta(db_ref, pojo_carta)

                    Utilidades.toastCorrutina(
                        thisActivity,
                        applicationContext,
                        "Carta modificada con exito"
                    )

                    val activity = Intent(applicationContext, HomeAdmin::class.java)
                    startActivity(activity)
                }

                imagen.setOnClickListener() {
                    accesoGaleria.launch("image/*")
                }

            }

            }
        volver.setOnClickListener {
            val activity = Intent(applicationContext, HomeAdmin::class.java)
            startActivity(activity)
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



