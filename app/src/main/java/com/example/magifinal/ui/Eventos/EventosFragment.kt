package com.example.magifinal.ui.Eventos

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magifinal.Ajustes
import com.example.magifinal.Autor
import com.example.magifinal.R
import com.example.magifinal.databinding.FragmentEventosBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EventosFragment : Fragment() {


    private var _binding: FragmentEventosBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var listaEventos: MutableList<Evento>
    private lateinit var fabAddEvento: FloatingActionButton
    private lateinit var adaptador: EventoAdaptador


    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_autor -> {

                val intent = Intent(activity, Autor::class.java)
                startActivity(intent)
                true
            }

            R.id.action_ajustes -> {
                val intent = Intent(activity, Ajustes::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEventosBinding.inflate(inflater, container, false)
        val root: View = _binding!!.root

        fabAddEvento = _binding!!.fabAddevento
        listaEventos = obtenerEventos()
        adaptador = EventoAdaptador(listaEventos)
        recyclerView = binding.recyclerViewEventos
        recyclerView.adapter = adaptador
        recyclerView.layoutManager = LinearLayoutManager(context)
        var sharedPreferences = requireActivity().getSharedPreferences("login", MODE_PRIVATE)
        var esAdmin = sharedPreferences.getBoolean("esAdmin", false)


        if (esAdmin) {
            fabAddEvento.visibility = View.VISIBLE

            fabAddEvento.setOnClickListener {
                val intent = Intent(activity, AnadirEvento::class.java)
                startActivity(intent)
            }
        } else {
            fabAddEvento.visibility = View.GONE
        }



        return root
    }

    private fun obtenerEventos(): MutableList<Evento> {
        val listaEventos = mutableListOf<Evento>()
        val db_ref = FirebaseDatabase.getInstance().reference


        db_ref.child("Tienda").child("Eventos").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaEventos.clear()
                snapshot.children.forEach { hijo: DataSnapshot ->
                    val pojo_evento = hijo.getValue(Evento::class.java)
                    listaEventos.add(pojo_evento!!)
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error en la lectura de datos", Toast.LENGTH_LONG).show()
            }
        })

        return listaEventos
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}