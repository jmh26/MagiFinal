package com.example.magifinal.Pedidos

import android.content.Context
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magifinal.R
import com.example.magifinal.Utilidades
import com.example.magifinal.ui.Cartas.Carta
import com.example.magifinal.ui.Cartas.CartaAdaptador
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class PedidoAdaptador(private val listaPedidos: MutableList<Pedido>): RecyclerView.Adapter<PedidoAdaptador.PedidoViewHolder>(),

    Filterable {
    private lateinit var contexto: Context


    class PedidoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre = itemView.findViewById<TextView>(R.id.item_nombreCartaPedido)
        val precio = itemView.findViewById<TextView>(R.id.item_precioCartaPedido)
        val imagen = itemView.findViewById<ImageView>(R.id.item_imagenCartaPedido)
        val eliminar = itemView.findViewById<ImageView>(R.id.item_eliminarCartaPedido)
        val confirmar = itemView.findViewById<ImageView>(R.id.preparado)
        val estadoPedido = itemView.findViewById<TextView>(R.id.estado)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PedidoAdaptador.PedidoViewHolder {
        contexto = parent.context
        val itemView = LayoutInflater.from(contexto).inflate(R.layout.item_pedido, parent, false)
        return PedidoAdaptador.PedidoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PedidoViewHolder, position: Int) {
        val pedidoActual = listaPedidos[position]
        holder.nombre.text = pedidoActual.nombre
        holder.estadoPedido.text = pedidoActual.estado
        holder.precio.text = pedidoActual.precio + "€"

        val url: String? = when (pedidoActual.imagen) {
            "" -> null
            else -> pedidoActual.imagen
        }


        Glide.with(contexto).load(url).apply(Utilidades.opcionesGlide(contexto))
            .transition(Utilidades.transicion).into(holder.imagen)

        var sharedPreferences = contexto.getSharedPreferences("login", Context.MODE_PRIVATE)
        var esAdmin = sharedPreferences.getBoolean("esAdmin", false)
        if (esAdmin) {
            holder.eliminar.visibility = View.VISIBLE
            holder.confirmar.visibility = View.VISIBLE

            holder.eliminar.setOnClickListener {
                try {
                    val db_ref = FirebaseDatabase.getInstance().reference
                    val st_ref = FirebaseStorage.getInstance().reference
                    listaPedidos.remove(pedidoActual)
                    Settings.Secure.getString(
                        contexto.contentResolver,
                        Settings.Secure.ANDROID_ID
                    )


                    st_ref.child("Pedidos").child("Fotos").child(pedidoActual.id!!).delete()
                    db_ref.child("Tienda").child("reservas_carta").child(pedidoActual.id!!)
                        .removeValue()
                    Toast.makeText(contexto, "Pedido eliminado", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(contexto, "Error al eliminar pedido", Toast.LENGTH_SHORT).show()
                }
            }

            holder.confirmar.setOnClickListener {
                val db_ref = FirebaseDatabase.getInstance().reference
                val pedidoID = pedidoActual.id ?: "" // Obtener el ID del pedido actual
                val nuevoEstado = "Preparado"

                db_ref.child("Tienda").child("reservas_carta").child(pedidoID).child("estado")
                    .setValue(nuevoEstado)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Eliminar el pedido de la lista local después de actualizar el estado en la base de datos
                            listaPedidos.remove(pedidoActual)
                            notifyDataSetChanged()
                            Toast.makeText(contexto, "Pedido preparado", Toast.LENGTH_SHORT).show()
                        } else {
                            Log.e("PedidoAdaptador", "Error al preparar pedido", task.exception)
                            Toast.makeText(contexto, "Error al preparar pedido", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        } else {
            holder.eliminar.visibility = View.GONE
            holder.confirmar.visibility = View.GONE





            }

        }
    override fun getItemCount(): Int = listaPedidos.size
    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}


