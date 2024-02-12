package com.example.magifinal

import android.net.Uri
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class AnadirCarta(override val coroutineContext: CoroutineContext) : AppCompatActivity(), CoroutineScope {


    private lateinit var nombre: EditText
    private lateinit var categoria: Spinner
    private lateinit var precio: EditText
    private lateinit var stock: EditText
    private lateinit var imagen: ImageView

    private lateinit var crear: Button
    private lateinit var volver: Button

    private lateinit var job: Job

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




    }



}