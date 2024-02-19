package com.example.magifinal.ui.Eventos

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magifinal.Ajustes
import com.example.magifinal.Autor
import com.example.magifinal.R
import com.example.magifinal.databinding.FragmentEventosBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EventosFragment : Fragment() {


    private var _binding: FragmentEventosBinding? = null

    private lateinit var recyclerView: RecyclerView
    private lateinit var listaEventos: MutableList<Evento>

    private lateinit var adaptador: EventoAdaptador
    private lateinit var db_ref: DatabaseReference
    private lateinit var lista: MutableList<Evento>


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

        listaEventos = obtenerEventos()
        adaptador = EventoAdaptador(listaEventos)
        recyclerView = _binding!!.recyclerViewEventos
        recyclerView.adapter = adaptador
        recyclerView.layoutManager = LinearLayoutManager(context)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}