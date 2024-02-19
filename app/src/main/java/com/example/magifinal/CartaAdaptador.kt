package com.example.magifinal

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.preference.PreferenceManager
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class CartaAdaptador (private val listaCartas: MutableList<Carta>): RecyclerView.Adapter<CartaAdaptador.CartaViewHolder>(),

    Filterable {
    private lateinit var contexto: Context


    private var listaCartasFiltrada = listaCartas

    class CartaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre = itemView.findViewById<TextView>(R.id.item_nombreCarta)
        val categoria = itemView.findViewById<TextView>(R.id.item_categoriaCarta)
        val precio = itemView.findViewById<TextView>(R.id.item_precioCarta)
        val stock = itemView.findViewById<TextView>(R.id.item_stockCarta)
        val imagen = itemView.findViewById<ImageView>(R.id.item_imagenCarta)
        val pedir = itemView.findViewById<ImageView>(R.id.item_pedirCarta)
        val eliminar = itemView.findViewById<ImageView>(R.id.item_eliminarCarta)
        val editar = itemView.findViewById<ImageView>(R.id.item_editarCarta)

    }


    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val busqueda = constraint.toString()
                if (busqueda.isEmpty()) {
                    listaCartasFiltrada = listaCartas
                } else {
                    val resultList = mutableListOf<Carta>()
                    for (row in listaCartas) {
                        if (row.nombre?.toLowerCase()!!.contains(busqueda.toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    listaCartasFiltrada = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = listaCartasFiltrada
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                listaCartasFiltrada = results?.values as MutableList<Carta>
                notifyDataSetChanged()
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CartaViewHolder {
        contexto = parent.context
        val itemView = LayoutInflater.from(contexto).inflate(R.layout.item_carta, parent, false)
        return CartaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartaAdaptador.CartaViewHolder, position: Int) {
        val cartaAct = listaCartasFiltrada[position]
        holder.nombre.text = cartaAct.nombre
        holder.categoria.text = "Categoria: "+ cartaAct.categoria
        holder.precio.text ="Precio: " +  cartaAct.precio
        holder.stock.text = "Stock: " + cartaAct.stock


        val url: String? = when (cartaAct.imagen) {
            null -> ""
            else -> cartaAct.imagen
        }



        Glide.with(contexto).load(url).apply(Utilidades.opcionesGlide(contexto))
            .transition(Utilidades.transicion).into(holder.imagen)




        val stockInt = cartaAct.stock?.toIntOrNull() ?: 0


        if (stockInt > 15) {
            holder.stock.setTextColor(ContextCompat.getColor(contexto, R.color.verde))
        } else if (stockInt in 1..15) {
            holder.stock.setTextColor(ContextCompat.getColor(contexto, R.color.amarillo))
        } else {
            holder.stock.setTextColor(ContextCompat.getColor(contexto, R.color.rojo))
        }


        var sharedPreferences = contexto.getSharedPreferences("login", MODE_PRIVATE)
        var esAdmin = sharedPreferences.getBoolean("esAdmin", false)




        Log.d("Adaptador", "Valor de tipoCuenta: $esAdmin")
        if (esAdmin) {
            holder.editar.visibility = View.VISIBLE
            holder.pedir.visibility = View.GONE
            holder.eliminar.visibility = View.VISIBLE
            holder.eliminar.setOnClickListener {
                try {
                    val db_ref = FirebaseDatabase.getInstance().reference
                    val st_ref = FirebaseStorage.getInstance().reference
                    listaCartasFiltrada.remove(cartaAct)
                    val androID =
                        Settings.Secure.getString(
                            contexto.contentResolver,
                            Settings.Secure.ANDROID_ID
                        )


                    st_ref.child("Cartas").child("Fotos").child(cartaAct.id!!).delete()
                    db_ref.child("Tienda").child("Cartas").child(cartaAct.id!!).removeValue()
                    Toast.makeText(contexto, "Carta eliminada", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(contexto, "Error al eliminar carta", Toast.LENGTH_SHORT).show()
                }
            }

            holder.editar.setOnClickListener {
                val intent = Intent(contexto, EditarCarta::class.java)
                intent.putExtra("carta", cartaAct)
                contexto.startActivity(intent)
            }


        } else {
            holder.editar.visibility = View.GONE
            holder.eliminar.visibility = View.GONE
            holder.pedir.setOnClickListener {
                val db_ref = FirebaseDatabase.getInstance().reference
                val user = FirebaseAuth.getInstance().currentUser
                val id = db_ref.push().key
                val stock = cartaAct.stock?.toIntOrNull()
                if (stock != null && stock > 0) {
                    val newStock = (stock - 1).toString()
                    cartaAct.stock = newStock
                    db_ref.child("Tienda").child("reservas_carta").child(id!!)
                        .setValue(cartaAct)
                    db_ref.child("Tienda").child("Cartas").child(cartaAct.id!!).child("stock")
                        .setValue(newStock)
                } else {
                    Toast.makeText(contexto, "No hay stock", Toast.LENGTH_SHORT).show()
                    holder.pedir.visibility = View.GONE
                }

            }
        }
    }




        override fun getItemCount(): Int = listaCartasFiltrada.size


    }



