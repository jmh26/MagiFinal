package com.example.magifinal

import android.content.Context
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
        holder.categoria.text = cartaAct.categoria
        holder.precio.text = cartaAct.precio
        holder.stock.text = cartaAct.stock

        val url: String? = when (cartaAct.imagen) {
            null -> ""
            else -> cartaAct.imagen
        }



        Glide.with(contexto).load(url).apply(Utilidades.opcionesGlide(contexto))
            .transition(Utilidades.transicion).into(holder.imagen)


        var sharedPreferences = PreferenceManager.getDefaultSharedPreferences(contexto)

        var tipoCuenta = sharedPreferences.getString("tipoCuenta", "cliente")

        if (tipoCuenta == "cliente") {
            holder.pedir.visibility = View.VISIBLE
            holder.pedir.setOnClickListener {
                val db_ref = FirebaseDatabase.getInstance().reference
                val user = FirebaseAuth.getInstance().currentUser
                val id = db_ref.push().key
                cartaAct.stock = (cartaAct.stock?.toInt()?.minus(1)).toString()
                val androID =
                    Settings.Secure.getString(contexto.contentResolver, Settings.Secure.ANDROID_ID)
                db_ref.child("Tienda").child("reservas_carta").child(androID).child(id!!)
                    .setValue(cartaAct)


            }


        } else {
            holder.pedir.visibility = View.GONE

            holder.eliminar.setOnClickListener {

                try {

                    val db_ref = FirebaseDatabase.getInstance().reference
                    val st_ref = FirebaseStorage.getInstance().reference
                    listaCartasFiltrada.remove(cartaAct)

                    val androID =
                        Settings.Secure.getString(contexto.contentResolver, Settings.Secure.ANDROID_ID)

                    st_ref.child("Cartas").child("Fotos").child(cartaAct.id!!).delete()
                    db_ref.child("Tienda").child("Cartas").child(cartaAct.id!!).removeValue()

                    Toast.makeText(contexto, "Carta eliminada", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {

                Toast.makeText(contexto, "Error al eliminar carta", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }




        override fun getItemCount(): Int = listaCartasFiltrada.size


    }



