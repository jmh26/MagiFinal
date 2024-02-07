package com.example.magifinal

import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CartaAdaptador (private val listaCartas: MutableList<Carta>): RecyclerView.Adapter<CartaAdaptador.CartaViewHolder>(),

    Filterable {


        private var listaCartasFiltrada = listaCartas
        class CartaViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
            val nombre = itemView.findViewById<TextView>(R.id.item_nombreCarta)
            val categoria = itemView.findViewById<TextView>(R.id.item_categoriaCarta)
            val precio = itemView.findViewById<TextView>(R.id.item_precioCarta)
            val stock = itemView.findViewById<TextView>(R.id.item_stockCarta)
            val imagen = itemView.findViewById<ImageView>(R.id.item_imagenCarta)
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
        val itemView = View.inflate(parent.context, R.layout.item_carta, null)
        return CartaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartaAdaptador.CartaViewHolder, position: Int) {
        val carta = listaCartasFiltrada[position]
        holder.nombre.text = carta.nombre
        holder.categoria.text = carta.categoria
        holder.precio.text = carta.precio
        holder.stock.text = carta.stock

        Glide.with(holder.itemView.context)
            .load(carta.imagen)
            .placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
            .into(holder.imagen)


    }

    override fun getItemCount(): Int = listaCartasFiltrada.size




}

