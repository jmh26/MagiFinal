package com.example.magifinal.Pedidos

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magifinal.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PedidosFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adaptador: PedidoAdaptador
    private lateinit var listaPedidos: MutableList<Pedido>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_pedidos, container, false)

        recyclerView = view.findViewById(R.id.recycler_view_pedidos)
        recyclerView.layoutManager = LinearLayoutManager(context)

        listaPedidos = obtenerPedidos()
        adaptador = PedidoAdaptador(listaPedidos)
        recyclerView.adapter = adaptador

        return view
    }



        private fun obtenerPedidos(): MutableList<Pedido> {
            val listaPedidos = mutableListOf<Pedido>()
            val db_ref = FirebaseDatabase.getInstance().reference
            val sharedPreferences = requireActivity().getSharedPreferences("login", MODE_PRIVATE)
            val esAdmin = sharedPreferences.getBoolean("esAdmin", false)

            if (esAdmin) {
                // Si el usuario es un administrador, obtén solo los pedidos que no están preparados
                db_ref.child("Tienda").child("reservas_carta").orderByChild("estado")
                    .equalTo("en preparación")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            listaPedidos.clear()
                            for (hijo in snapshot.children) {
                                val pedido = hijo.getValue(Pedido::class.java)
                                if (pedido != null) {
                                    listaPedidos.add(pedido)
                                }
                            }
                            adaptador.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                context,
                                "Error en la lectura de datos",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            } else {
                val user = FirebaseAuth.getInstance().currentUser
                db_ref.child("Tienda").child("reservas_carta").orderByChild("clienteID")
                    .equalTo(user?.uid)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            listaPedidos.clear()
                            for (hijo in snapshot.children) {
                                val pedido = hijo.getValue(Pedido::class.java)
                                if (pedido != null) {
                                    listaPedidos.add(pedido)
                                }
                            }
                            adaptador.notifyDataSetChanged()
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(
                                context,
                                "Error en la lectura de datos",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    })
            }

            return listaPedidos
        }
    }