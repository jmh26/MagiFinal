package com.example.magifinal.ui.Cartas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magifinal.Carta
import com.example.magifinal.CartaAdaptador
import com.example.magifinal.Evento
import com.example.magifinal.databinding.FragmentCartasBinding
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
    private var contextoapp  = this.context



    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        var db_ref = FirebaseDatabase.getInstance().reference

        var user = FirebaseAuth.getInstance().currentUser

        lista = mutableListOf()
        _binding = FragmentCartasBinding.inflate(inflater, container, false)

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
                Toast.makeText(contextoapp, "Error en la lectura de datos", Toast.LENGTH_LONG).show()
            }



        })

        adaptador = CartaAdaptador(lista)
        recyclerView = binding.recyclerViewCartas
        recyclerView.adapter = adaptador

        recyclerView.layoutManager = LinearLayoutManager(contextoapp)



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}