package com.example.magifinal.ui.Cartas

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magifinal.Ajustes
import com.example.magifinal.Autor
import com.example.magifinal.R
import com.example.magifinal.databinding.FragmentCartasBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartasFragment : Fragment() {

    private var _binding: FragmentCartasBinding? = null
    private lateinit var lista: MutableList<Carta>
    private lateinit var adaptador: CartaAdaptador
    private lateinit var recyclerView: RecyclerView
    private var contextoapp = this.context
    private lateinit var fabAddCarta: FloatingActionButton


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

        _binding = FragmentCartasBinding.inflate(inflater, container, false)
        lista = mutableListOf()


        fabAddCarta = binding.fabAddcarta

        var sharedPreferences = requireActivity().getSharedPreferences("login", MODE_PRIVATE)
        var esAdmin = sharedPreferences.getBoolean("esAdmin", false)

        if (esAdmin) {
            fabAddCarta.visibility = View.VISIBLE
            fabAddCarta = binding.fabAddcarta

            fabAddCarta.setOnClickListener {
                val intent = Intent(activity, AnadirCarta::class.java)
                startActivity(intent)
            }
        } else {
            fabAddCarta.visibility = View.GONE
        }


        var db_ref = FirebaseDatabase.getInstance().reference






        db_ref.child("Tienda").child("Cartas").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                snapshot.children.forEach { hijo: DataSnapshot ->
                    val pojo_carta = hijo.getValue(Carta::class.java)
                    lista.add(pojo_carta!!)
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(contextoapp, "Error en la lectura de datos", Toast.LENGTH_LONG)
                    .show()
            }


        })

        adaptador = CartaAdaptador(lista)
        recyclerView = binding.recyclerViewCartas
        recyclerView.adapter = adaptador

        recyclerView.layoutManager = LinearLayoutManager(contextoapp)


        return binding.root


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fabCarrito: FloatingActionButton = view.findViewById(R.id.fab_carrito)
        fabCarrito.setOnClickListener {
            findNavController().navigate(R.id.action_cartasFragment_to_pedidosFragment)
        }
    }
}